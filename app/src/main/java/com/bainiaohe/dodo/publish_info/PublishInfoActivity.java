package com.bainiaohe.dodo.publish_info;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bainiaohe.dodo.R;
import com.bainiaohe.dodo.utils.URLConstants;
import com.bainiaohe.dodo.utils.UserService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import org.apache.http.Header;

import java.util.ArrayList;

/**
 * 发布info 页面
 */
public class PublishInfoActivity extends ActionBarActivity {

    private static final int PICK_PICTURE_RESULT_CODE = 1155;
    private static final String TAG = "PublishInfoActivity";
    //输入框
    private EditText inputText = null;

    /**
     * 用于标记从图库中选完图片后该把图片加载到哪个image view
     */
    private int selectedPhotoPlaceHolderIndex = 0;

    //用于显示选取的图片
    private ImageView[] photoPlaceHolders = new ImageView[3];

    /**
     * 存放已选中的图片的路径
     */
    private ArrayList<String> selectedPhotoPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_info);

        initViews();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //显示添加的图片
        if (requestCode == PICK_PICTURE_RESULT_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            Cursor cursor = managedQuery(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            //根绝索引值获取图片路径
            selectedPhotoPaths.add(cursor.getString(columnIndex));

            Picasso.with(this)
                    .load(uri)
                    .resizeDimen(R.dimen.medium_picture_size, R.dimen.medium_picture_size).centerInside()
                    .into(photoPlaceHolders[selectedPhotoPlaceHolderIndex]);
        }
    }

    /**
     * 初始化UI
     */
    private void initViews() {
        inputText = (EditText) findViewById(R.id.inputText);

        photoPlaceHolders[0] = (ImageView) findViewById(R.id.photo1);
        photoPlaceHolders[1] = (ImageView) findViewById(R.id.photo2);
        photoPlaceHolders[2] = (ImageView) findViewById(R.id.photo3);

        for (int i = 0; i < photoPlaceHolders.length; i++) {
            final int temp = i;
            photoPlaceHolders[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickPhotoFromGallery();
                    selectedPhotoPlaceHolderIndex = temp;
                }
            });
        }
    }

    /**
     * 从本地相册选取图片
     */
    private void pickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PICTURE_RESULT_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.publish_info) {
            //todo 发表状态
            publishInfo();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发表状态
     */
    private void publishInfo() {
        //发表
        String text = inputText.getText().toString();

        RequestParams params = new RequestParams();

        params.put("id", UserService.userId);
        params.put("message", text);

        //todo 上传图片
        for (int i = 0; i < selectedPhotoPaths.size(); i++) {
            Log.e(TAG, "path:" + selectedPhotoPaths.get(i));
            params.add("images", selectedPhotoPaths.get(i));
        }


//                params.put("images", images);//TODO 上传图片

        Log.e(TAG, "Params:" + params);

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(URLConstants.POST_INFO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e(TAG, "success:" + statusCode);
                Toast.makeText(PublishInfoActivity.this, R.string.publish_succeed, Toast.LENGTH_SHORT).show();
                finish();//TODO 向main activity中插入新发布的状态
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(PublishInfoActivity.this, R.string.publish_succeed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
