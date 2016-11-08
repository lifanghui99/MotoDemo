package example.androidstudy.com.motodemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cneop.util.ImageUtil;
import com.cneop.util.PopupDailog;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText  edit_barcode=null;
    private LinearLayout  linearLayout=null;
    private ImageView image=null;
    private HashMap<String, View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();

    }

    public void initView()
    {
        edit_barcode = (EditText) this.findViewById(R.id.edit_barcode);
        linearLayout = (LinearLayout) this.findViewById(R.id.linearLayout);
        image = (ImageView) this.findViewById(R.id.picture);

        views = new HashMap<String, View>();
    }

    public void initListener()
    {
        ScanDataBarcodeReceiver receiver=new ScanDataBarcodeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.geenk.action.GET_SCANDATA");
        this.registerReceiver((BroadcastReceiver) receiver, intentFilter);
    }
    public  void insertView(String barcodeStr,Bitmap bitmap){

        View inflate = View.inflate( MainActivity.this , R.layout.scan_barcode_qs_listview_item, (ViewGroup) null);
        TextView tvBarcode = (TextView) inflate.findViewById(R.id.barcode);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image);
        TextView tvOperation = (TextView) inflate.findViewById(R.id.qs_caozuo);



        tvOperation.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String barcode=((TextView) ((View) view.getParent()).findViewById(R.id.barcode)).getText()
                        .toString();
                deleteBarcode(barcode);
            }
        });
        imageView.setOnClickListener((View.OnClickListener) new View.OnClickListener() {
            public void onClick(final View view) {
                String barcode=((TextView) ((View) view.getParent()).findViewById(R.id.barcode)).getText()
                        .toString();
                final BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
                bitmapFactoryOptions.inSampleSize = 2;
                String strFile= Environment.getExternalStorageDirectory() +  "/temp1/"+barcode+".png";

                final Bitmap decodeFile = BitmapFactory
                        .decodeFile(strFile, bitmapFactoryOptions);
                View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.qs_image,
                        (ViewGroup) null);
                final PopupDailog popupDailog = new PopupDailog(MainActivity.this);
                ((RelativeLayout) inflate.findViewById(R.id.relativeLayout)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View view) {
                        if (popupDailog != null) {
                            popupDailog.dimissPopup();
                        }
                    }
                });
                ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_image);
                imageView.setImageBitmap(decodeFile);
                 popupDailog.showPopuptWindow(inflate, getWindow().getDecorView());
            }
        });



        if ( bitmap != null) {
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 8,
                    bitmap.getHeight() / 8, false));
        }
        tvBarcode.setText(barcodeStr);
        this.linearLayout.addView(inflate, 0);
        views.put(barcodeStr, inflate);

    }
    public void deleteBarcode(final String barcode) {
        if (this.views != null && this.views.size() > 0) {
            final View view = this.views.remove(barcode);
            if (view != null) {
                this.linearLayout.removeView(view);
            }
        }
    }

    public class ScanDataBarcodeReceiver extends BroadcastReceiver {
        public void onReceive(Context ctx, Intent intent) {
            if (intent.getAction().equals("com.geenk.action.GET_SCANDATA")) {
                String DEFAULT_SAVE_IMAGE_PATH_TEMP = Environment.getExternalStorageDirectory() +  "/temp/temp.png";

                String barcode = new String(intent.getExtras().getByteArray("data")).trim();
                String strDest = Environment.getExternalStorageDirectory() +  "/temp1/"+barcode+".png";
                Bitmap bmp = ImageUtil.getBitmapFromPath( DEFAULT_SAVE_IMAGE_PATH_TEMP,1,1 );

                insertView(barcode,bmp);
                String strDir = Environment.getExternalStorageDirectory() +  "/temp1";
                File file=new File(strDir);
                if(!file.exists())
                    file.mkdir();

                ImageUtil.compressBmpFileToFile(DEFAULT_SAVE_IMAGE_PATH_TEMP,100,strDest  );
            }
        }
    }
}

