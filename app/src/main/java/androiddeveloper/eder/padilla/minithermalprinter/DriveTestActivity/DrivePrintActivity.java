package androiddeveloper.eder.padilla.minithermalprinter.DriveTestActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import androiddeveloper.eder.padilla.minithermalprinter.BluetoothService;
import androiddeveloper.eder.padilla.minithermalprinter.R;
import androiddeveloper.eder.padilla.minithermalprinter.command.sdk.Command;
import androiddeveloper.eder.padilla.minithermalprinter.command.sdk.PrintPicture;
import androiddeveloper.eder.padilla.minithermalprinter.command.sdk.PrinterCommand;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrivePrintActivity extends AppCompatActivity {
    private static final String TAG = "DriveTestActivity";
    //mac Address 0F:02:17:90:69:49
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_CHOSE_BMP = 3;
    private static final int REQUEST_CAMER = 4;

    //QRcode
    private static final int QR_WIDTH = 350;
    private static final int QR_HEIGHT = 350;
    /*******************************************************************************************************/
    private static final String CHINESE = "GBK";
    private static final String THAI = "CP874";
    private static final String KOREAN = "EUC-KR";
    private static final String BIG5 = "BIG5";
    private static final boolean DEBUG = true;
    // Member object for the services
    private BluetoothService mService = null;

    private static int codePage,withTimes,heightTimes,fontType;

    @BindView(R.id.seekBarCodePage)
    SeekBar seekBarCodePage;

    @BindView(R.id.seekBarWithPages)
    SeekBar seekBarWithPages;

    @BindView(R.id.seekBarHeightPages)
    SeekBar seekBarHeightPages;

    @BindView(R.id.seekBarFontType)
    SeekBar seekBarFontType;

    @BindView(R.id.tv_codepage)
    TextView tv_codepage;

    @BindView(R.id.tv_with_pages)
    TextView tv_with_pages;

    @BindView(R.id.tv_height_pages)
    TextView tv_height_pages;

    @BindView(R.id.tv_font_type)
    TextView tv_font_type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_print);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
       initSeekbarsListeners();
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Conexión Bluetooth no disponible",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        if (BluetoothAdapter.checkBluetoothAddress("0F:02:17:90:69:49")) {
            BluetoothDevice device = mBluetoothAdapter
                    .getRemoteDevice("0F:02:17:90:69:49");
            // Attempt to connect to the device
            mService = new BluetoothService(this, mHandler);
            mService.connect(device);
        }
    }

    private void initSeekbarsListeners() {
        seekBarCodePage.setMax(0);
        seekBarWithPages.setMax(3);
        seekBarHeightPages.setMax(3);
        seekBarFontType.setMax(15);
        seekBarCodePage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_codepage.setText(": "+i);
                codePage = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarWithPages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_with_pages.setText(": "+i);
                withTimes = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarHeightPages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_height_pages.setText(": "+i);
                heightTimes = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarFontType.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_font_type.setText(": "+i);
                fontType = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        }

        //mService = new BluetoothService(this, mHandler);
    }
    @Override
    public synchronized void onResume() {
        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
    }


    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.e(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(),R.string.title_connected_to,Toast.LENGTH_SHORT).show();
                            //pinterConectedTest();
                            //printLogo();
                            //createImage();
                            //formatTest();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(getApplicationContext(),R.string.title_connecting,Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Toast.makeText(getApplicationContext(),R.string.title_not_connected,Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:
                    Toast.makeText(getApplicationContext(), "Se perdio la conexión con el sipositivo",
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "No se pudo conectar ocn el dispositivo",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void pinterConectedTest() {
        String msg = "Congratulations!\n\n";
        String data = "You have sucessfully created communications between your device and our bluetooth printer.\n"
                +"the company is a high-tech enterprise which specializes" +
                "in R&D,manufacturing,marketing of thermal printers and barcode scanners.\n\n";
        Command.ESC_Align[2] = 0x01;
        SendDataByte(Command.ESC_Align);
        SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 1, 1, 4));
        Command.GS_ExclamationMark[2] = 0x11;
        SendDataByte(Command.GS_ExclamationMark);
        SendDataByte(PrinterCommand.POS_Print_Text(data, CHINESE, 0, 0, 0, 5));
        SendDataByte(PrinterCommand.POS_Set_Cut(1));
        SendDataByte(PrinterCommand.POS_Set_PrtInit());
    }

    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mService.write(data);
    }
    //OnClick(R.id.floatingActionButton)
    //ublic void printTicket(){
    //   SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd/ HH:mm:ss ");
    //   Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    //   String str = formatter.format(curDate);
    //   String date = str + "\n\n\n\n\n\n";
    //       try {
    //           printLogo();
    //           byte[] qrcode = PrinterCommand.getBarCommand("Aqui va el código DRIVE del usuario!", 0, 3, 6);//
    //           Command.ESC_Align[2] = 0x01;
    //           SendDataByte(Command.ESC_Align);
    //           SendDataByte(qrcode);

    //           SendDataByte(Command.ESC_Align);
    //           Command.GS_ExclamationMark[2] = 0x11;
    //           SendDataByte(Command.GS_ExclamationMark);
    //           SendDataByte("DRIVE MX\n".getBytes("GBK"));
    //           Command.ESC_Align[2] = 0x00;
    //           SendDataByte(Command.ESC_Align);
    //           Command.GS_ExclamationMark[2] = 0x00;
    //           SendDataByte(Command.GS_ExclamationMark);
    //           SendDataByte("Número  888888\nRecibo  S00003333\nCajero 1001\nFecha：xxxx-xx-xx\nPrint Time：xxxx-xx-xx  xx:xx:xx\n".getBytes("GBK"));
    //           SendDataByte("Nombre    Cantidad    Precio  Dinero\nShoes   10.00       899     8990\nBall    10.00       1599    15990\n".getBytes("GBK"));
    //           SendDataByte("Cantidad             20.00\nTotal                16889.00\nPago：              17000.00\nKeep the change：      111.00\n".getBytes("GBK"));
    //           SendDataByte("Compañia：DRIVE MX\nSite：www.driveapp.mx\nDirección：Avenida Vasco de Quiroga 3800, Contadero, 05109 CDMX\nphone number：0755-11111111\nHelpline：400-xxx-xxxx\n================================\n".getBytes("GBK"));
    //           Command.ESC_Align[2] = 0x01;
    //           SendDataByte(Command.ESC_Align);
    //           Command.GS_ExclamationMark[2] = 0x11;
    //           SendDataByte(Command.GS_ExclamationMark);
    //           SendDataByte("Welcome again!\n".getBytes("GBK"));
    //           Command.ESC_Align[2] = 0x00;
    //           SendDataByte(Command.ESC_Align);
    //           Command.GS_ExclamationMark[2] = 0x00;
    //           SendDataByte(Command.GS_ExclamationMark);
    //           printStores();
    //           SendDataByte("(The above information is for testing template, if agree, is purely coincidental!)\n".getBytes("GBK"));
    //           Command.ESC_Align[2] = 0x02;
    //           SendDataByte(Command.ESC_Align);
    //           SendDataString(date);
    //           SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));
    //           SendDataByte(Command.GS_V_m_n);

    //       } catch (UnsupportedEncodingException e) {
    //           // TODO Auto-generated catch block
    //           e.printStackTrace();
    //       }
    //   }
    @OnClick(R.id.floatingActionButton)
    public void printSeekbarValues(){
        try {
            printLogo();
            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("VIA SANTA FE\n".getBytes("GBK"));
            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[1] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("VALET\n PARKING\n".getBytes("GBK"));
            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[1] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("No.: 3129478056\n".getBytes("GBK"));
            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[1] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("PRESENTE ESTE BOLETO PARA\n RECLAMAR SU VEHICULO\n".getBytes("GBK"));


            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(PrinterCommand.POS_Print_Text("Fecha: 14 Sep 2017 23:12", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Placa: 7293     Color: Gris", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Marca: BMW      Modelo: 500", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Objetos Reportados:", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("1 Laptop", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("1 Lentes", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Notas:", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Daños:", CHINESE, 0, 0, 0, 0));

            // Imprimir la imagen del carro
            // imprimeDanos();

            // TODO: camibar nombre a ImprimeQR()
            createImage();

            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[1] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("Paga el estacionamiento con Drive\n Descargala como Drive App\n".getBytes("GBK"));

            // TODO: Imprimir la imagen mas pequeña
            printStores();

            // TODO: Imprimir el contrato -> Contrato.bmp
            // imprimeContrato();


            // hasta aqui es el primer tanto

            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(248));//Esto es el espacio que se le da al fondo del ticket
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(PrinterCommand.POS_Print_Text("Fecha: 14 Sep 2017 23:12", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Placa: 7293     Color: Gris", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);
            SendDataByte(PrinterCommand.POS_Print_Text("Marca: BMW      Modelo: 500", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);

            // hasta aqui es el segundo tanto


            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(248));//Esto es el espacio que se le da al fondo del ticket
            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[1] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("No.: 3129478056\n".getBytes("GBK"));

            // hasta aqui es el tercer tanto

            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(248));//Esto es el espacio que se le da al fondo del ticket

            SendDataByte(Command.GS_V_m_n);//
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void SendDataString(String data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void printLogo(){

        //	byte[] buffer = PrinterCommand.POS_Set_PrtInit();
        //Bitmap mBitmap = ((BitmapDrawable) imageViewPicture.getDrawable())
        //        .getBitmap();
        Drawable myDrawable = getResources().getDrawable(R.drawable.Logo_Entra);
        Bitmap mBitmap      = ((BitmapDrawable) myDrawable).getBitmap();
        int nMode = 0;
        int nPaperWidth ;
        //if(width_58mm.isChecked())
            nPaperWidth = 384;
        //else if (width_80.isChecked())
        //    nPaperWidth = 576;
        if(mBitmap != null) {
            /**
             * Parameters:
             * mBitmap  要打印的图片
             * nWidth   打印宽度（58和80）
             * nMode    打印模式
             * Returns: byte[]
             */
            byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
            //	SendDataByte(buffer);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }
    private void printStores(){
        Drawable myDrawable = getResources().getDrawable(R.drawable.drive_stores);
        Bitmap mBitmap      = ((BitmapDrawable) myDrawable).getBitmap();
        int nMode = 0;
        int nPaperWidth ;
        //if(width_58mm.isChecked())
        nPaperWidth = 384;
        //else if (width_80.isChecked())
        //    nPaperWidth = 576;
        if(mBitmap != null) {
            /**
             * Parameters:
             * mBitmap  要打印的图片
             * nWidth   打印宽度（58和80）
             * nMode    打印模式
             * Returns: byte[]
             */
            byte[] data = PrintPicture.POS_PrintBMP(mBitmap, nPaperWidth, nMode);
            //	SendDataByte(buffer);
            SendDataByte(Command.ESC_Init);
            SendDataByte(Command.LF);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        }
    }
    private void createImage() {
        try {
            // 需要引入zxing包
            QRCodeWriter writer = new QRCodeWriter();

            String text = "valet/No.Ticket";

            Log.i(TAG, "生成的文本：" + text);
            if (text == null || "".equals(text) || text.length() < 1) {
                Toast.makeText(this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
                return;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            byte[] data = PrintPicture.POS_PrintBMP(bitmap, 384, 0);
            SendDataByte(data);
            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(30));
            SendDataByte(PrinterCommand.POS_Set_Cut(1));
            SendDataByte(PrinterCommand.POS_Set_PrtInit());
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void formatTest(){
        try {
            Command.ESC_Align[2] = 0x01;
            SendDataByte(Command.ESC_Align);

            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x11;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte("DRIVE MX\n".getBytes("GBK"));
            Command.ESC_Align[2] = 0x00;
            SendDataByte(Command.ESC_Align);
            Command.GS_ExclamationMark[2] = 0x00;
            SendDataByte(Command.GS_ExclamationMark);
            SendDataByte(PrinterCommand.POS_Print_Text("Código : MX", CHINESE, 0, 0, 0, 0));
            SendDataByte(Command.LF);

            SendDataByte(PrinterCommand.POS_Set_PrtAndFeedPaper(48));//Esto es el espacio que se le da al fondo del ticket
            SendDataByte(Command.GS_V_m_n);//
        }
        catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    }
    private void seekBarTest(){

    }

}
