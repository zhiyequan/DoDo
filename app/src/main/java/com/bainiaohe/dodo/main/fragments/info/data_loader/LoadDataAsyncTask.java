package com.bainiaohe.dodo.main.fragments.info.data_loader;

import android.os.AsyncTask;

/**
 * 多个task同时执行时可以防止重复加载数据的异步任务
 * Created by zhugongpu on 15/1/24.
 */
public class LoadDataAsyncTask extends AsyncTask<Void, Void, Void> {

    /**
     * 标记是否正在加载数据
     * 防止多个task同时执行时重复加载数据
     */
    private static boolean isLoadingData = false;
    private DataLoader dataLoader = null;

    public LoadDataAsyncTask(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        if (!isLoadingData) {//如果不在加载数据，则设置为正在加载
            isLoadingData = true;
            dataLoader.doInBackground();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (isLoadingData)
            dataLoader.onPostExecute();
        isLoadingData = false;//数据已经加载完成
    }


    public interface DataLoader {
        public void doInBackground();

        public void onPostExecute();
    }


}
