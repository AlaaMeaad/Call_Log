package softwarecity.net.calllog.remote;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import softwarecity.net.calllog.CallLogs;
import softwarecity.net.calllog.api.ApiServers;

public class DataManagerImpl implements DataManager {

    ApiServers apiService;

    public DataManagerImpl() {
        apiService = ApiClient.getClient().create(ApiServers.class);
    }


    @Override
    public void callLogs(final RetrofitCallback callback, String data) {
        Call<CallLogs> call = apiService.callLogs(data);
        call.enqueue(new Callback<CallLogs>() {
            @Override
            public void onResponse(Call<CallLogs> call, Response<CallLogs> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<CallLogs> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
