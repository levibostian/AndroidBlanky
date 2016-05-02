package com.levibostian.androidblanky.manager.api;

import com.crashlytics.android.Crashlytics;
import com.levibostian.androidblanky.service.GitHubApi;
import com.levibostian.androidblanky.vo.StatusMessageVo;
import com.levibostian.androidblanky.vo.error.FieldsMissingVo;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;

public abstract class BaseApiManager {

    protected GitHubApi mGitHubApi;
    protected Retrofit mRetrofit;

    public interface ApiResponseCallback<DATA> {
        void success(DATA data);
        void apiError(String message);
        void failure(String message);
    }

    public BaseApiManager(GitHubApi api, Retrofit retrofit) {
        mGitHubApi = api;
        mRetrofit = retrofit;
    }

    // Retrofit considers a success differently then I do. Therefore, I have to check the response code to decide for myself.
    protected final void processApiResponse(Response response, ApiResponseCallback callback) {
        if (response.isSuccessful()) {
            callback.success(response.body());
        } else {
            if (response.code() >= 500) {
                callback.apiError("Error occurred. Come back later and try again.");
            } else if (response.code() == 422) {
                Converter<ResponseBody, FieldsMissingVo> errorConverter = mRetrofit.responseBodyConverter(FieldsMissingVo.class, new Annotation[0]);
                try {
                    FieldsMissingVo error = errorConverter.convert(response.errorBody());

                    callback.apiError(error.errors[0].msg);
                } catch (IOException e) {
                    Crashlytics.logException(e);

                    callback.apiError("Error processing request. Try again.");
                }
            } else {
                Converter<ResponseBody, StatusMessageVo> errorConverter = mRetrofit.responseBodyConverter(StatusMessageVo.class, new Annotation[0]);
                try {
                    StatusMessageVo error = errorConverter.convert(response.errorBody());

                    callback.apiError(error.message);
                } catch (IOException e) {
                    Crashlytics.logException(e);

                    callback.apiError("Error processing request. Try again.");
                }
            }
        }
    }

}
