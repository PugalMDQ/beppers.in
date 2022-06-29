package com.mdq.social.utils;


import com.mdq.social.app.data.app.AppController;
import com.mdq.social.app.data.exception.AppException;
import com.mdq.social.app.helper.error.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import retrofit2.Response;
import rx.Completable;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxJavaUtils {

    /*Exceptions*/
    private static final String EXCEPTION_NO_NETWORK_CONNECTION = "No Internet connection";
    private static final String EXCEPTION_NO_NETWORK_MESSAGE = "Please check your connection or try again later";
    private static final String EXCEPTION_REQUEST_TIMEOUT = "Request timed out";
    private static final String EXCEPTION_PLEASE_TRY_AGAIN = "Something went wrong, please try again after some time.";
    private static final String API_UNKNOWN_FAILURE_MSG = "Something went wrong";

    public static <T> Observable.Transformer<T, T> applyObserverSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable.Transformer applyCompletableSchedulers() {
        return completable -> completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> Observable.Transformer<T, T> applyErrorTransformer() {
        return observable -> observable.onErrorResumeNext(throwable -> {
            if (!NetworkUtils.INSTANCE.isConnected(AppController.Companion.getInstance())) {
                return Observable.error(new RuntimeException(EXCEPTION_NO_NETWORK_CONNECTION));
            } else if (throwable instanceof UnknownHostException) {
                return Observable.error(new RuntimeException(EXCEPTION_REQUEST_TIMEOUT));
            } else if (throwable instanceof SocketTimeoutException) { // Slow Internet Connection
                return Observable.error(new RuntimeException(EXCEPTION_REQUEST_TIMEOUT));
            } else if (throwable instanceof com.google.gson.JsonSyntaxException) {
                LoggerUtils.INSTANCE.d("JSON_SYNTAX", throwable);
                return Observable.error(throwable);
            } else {
                if (throwable instanceof HttpException) {
                    Response response = ((HttpException) throwable).response();
                    if (AppException.Companion.isAppException(response)) {
                        try {
                            Gson gson = new GsonBuilder().serializeNulls().create();
                            ErrorResponse errorResponse = gson.fromJson(AppException.Companion.create(response).getMessage(), ErrorResponse.class);
                            if (errorResponse != null) {
                                return Observable.error(new Throwable(errorResponse.getErrorDescription()));
                            } else {
                                return Observable.error(new Throwable(API_UNKNOWN_FAILURE_MSG));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return Observable.error(throwable);
                        }
                    }
                    return Observable.error(throwable);
                } else return Observable.error(throwable);
            }
        });
    }

    public static <T> Observable.Transformer<T, T> applyOnErrorCrasher() {
        return observable -> observable.doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                final Throwable checkpoint = new Throwable();
                StackTraceElement[] stackTrace = checkpoint.getStackTrace();
                StackTraceElement element = stackTrace[1]; // First element after `crashOnError()`
                String msg = String.format("onError() crash from subscribe() in %s.%s(%s:%s)",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getFileName(),
                        element.getLineNumber());

                throw new OnErrorNotImplementedException(msg, throwable);
            }
        });
    }
}
