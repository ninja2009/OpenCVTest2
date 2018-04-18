package com.example.hagen.opencvtest2;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {
    private static final String TAG = "MainActivity";

    private CameraBridgeViewBase mOpenCvCameraView;

    Mat mRgba, imgHSV, imgThresholded, imgRed, imgBlue, imgGreen, imgYellow, imgPurple, imgTurquoise, imgColor, image;

    Rect rect, colorRect;
    int whitePixelRed, whitePixelGreen, whitePixelBlue, whitePixelYellow, whitePixelPurple, whitePixelTurquoise;

    int lowH = 115; //red = 105, green = 45, blue = 0
    int heighH = 125; //red = 135, green = 75, blue = 10
    int lowS = 20;
    int heighS = 255;
    int lowL = 10;
    int heighL = 255;

    String bitString = "";

    String ausgabe = "", ausgabeAlt = "";

    String grandpa = "", father = "", son = "";
    boolean algo = true;
    boolean begin = false;
    boolean change = false;


    int count = 0,  berCount = 0;

    int rHeight = 40;
    int rWidth = 40;

    boolean blau = false, rot = false, weiss = false, gruen = false, gelb = false, lila = false, tuekis = false;

    Scalar sc1, sc2;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.JCV);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.enableFpsMeter();

        sc1 = new Scalar(lowH,lowS,lowL);
        sc2 = new Scalar(heighH,heighS,heighL);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initDebug();
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height)
    {
        rHeight = height/12;
        rWidth = width/16;

        mRgba = new Mat(height, width, CvType.CV_8UC4);

        imgRed = new Mat(rHeight,rWidth, mRgba.type());
        imgBlue= new Mat(rHeight,rWidth, mRgba.type());
        imgGreen= new Mat(rHeight,rWidth, mRgba.type());
        imgYellow= new Mat(rHeight,rWidth, mRgba.type());
        imgPurple= new Mat(rHeight,rWidth, mRgba.type());
        imgTurquoise= new Mat(rHeight,rWidth, mRgba.type());
        imgHSV = new Mat(rHeight,rWidth, CvType.CV_8UC4);

        imgThresholded = new Mat(rHeight,rWidth, CvType.CV_8UC4);
        imgColor = new Mat(rHeight,rWidth, CvType.CV_8UC4);
    }

    public void onCameraViewStopped()
    {
        mRgba.release();
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame)
    {

        mRgba = inputFrame.rgba();

        rect = new Rect(mRgba.cols() / 2 - rWidth / 2, mRgba.rows() / 2 - rHeight / 2, rWidth, rHeight);
        imgThresholded = mRgba.submat(rect);
        colorRect = new Rect(0, 0, rWidth, rHeight);
        imgColor = mRgba.submat(colorRect);

        Imgproc.rectangle(mRgba, new Point(mRgba.cols() / 2 - rWidth / 2, mRgba.rows() / 2 - rHeight / 2), new Point(mRgba.cols() / 2 + rWidth / 2, mRgba.rows() / 2 + rHeight / 2), new Scalar(250, 250, 250), 1);


        //Imgproc.cvtColor(mRgba, imgHSV, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(imgThresholded, imgHSV, Imgproc.COLOR_BGR2HSV);

        Core.inRange(imgHSV, new Scalar(100, 20, 10), new Scalar(130, 255, 255), imgRed);
        Core.inRange(imgHSV, new Scalar(0, 20, 10), new Scalar(10, 255, 255), imgBlue);
        Core.inRange(imgHSV, new Scalar(55, 30, 15), new Scalar(65, 255, 255), imgGreen);
        Core.inRange(imgHSV, new Scalar(80, 50, 50), new Scalar(90, 255, 255), imgYellow);
        Core.inRange(imgHSV, new Scalar(140, 20, 10), new Scalar(160, 255, 255), imgPurple);
        Core.inRange(imgHSV, new Scalar(27, 20, 10), new Scalar(33, 255, 255), imgTurquoise);


        whitePixelRed = Core.countNonZero(imgRed);
        whitePixelGreen = Core.countNonZero(imgGreen);
        whitePixelBlue = Core.countNonZero(imgBlue);
        whitePixelYellow = Core.countNonZero(imgYellow);
        whitePixelPurple = Core.countNonZero(imgPurple);
        whitePixelTurquoise = Core.countNonZero(imgTurquoise);

        /*Log.d(TAG, "Red: " + String.valueOf(whitePixelRed));
        Log.d(TAG, "Green: " + String.valueOf(whitePixelGreen));
        Log.d(TAG, "Blue: " + String.valueOf(whitePixelBlue));
        Log.d(TAG, "Yellow: " + String.valueOf(whitePixelYellow));
        Log.d(TAG, "Purple: " + String.valueOf(whitePixelPurple));
        Log.d(TAG, "Turquoise: " + String.valueOf(whitePixelTurquoise));*/


        //detectWithout();
        polReturnToZeroDecTwoBit();

        image.copyTo(imgColor);

        return mRgba;
    }

    public void detectWithout()
    {
        grandpa = father;
        father = son;

        if (whitePixelRed < 188 && whitePixelGreen < 217 && whitePixelBlue < 308 && whitePixelYellow < 130 && whitePixelPurple < 190 && whitePixelTurquoise < 95)// last Average values from test divided by 2
        {
            image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 250, 250));
            son = "white";
        } else {
            if (whitePixelRed > whitePixelGreen && whitePixelRed > whitePixelBlue&& whitePixelRed > whitePixelYellow && whitePixelRed > whitePixelPurple  && whitePixelRed > whitePixelTurquoise) {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 0, 0));
                son = "red";
                begin = true;
            } else if (whitePixelGreen > whitePixelBlue && whitePixelGreen > whitePixelYellow && whitePixelGreen > whitePixelPurple && whitePixelGreen > whitePixelTurquoise) {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 250, 0));
                Log.d(TAG, "Green " + count);
                son = "green";
            } else if (whitePixelBlue > whitePixelYellow && whitePixelBlue > whitePixelPurple && whitePixelBlue > whitePixelTurquoise){
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 0, 250));
                Log.d(TAG, "Blue " + count);
                son = "blue";
            } else if(whitePixelYellow > whitePixelPurple && whitePixelYellow > whitePixelTurquoise)
            {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 250, 0));
                Log.d(TAG, "Yellow " + count);
                son = "yellow";
            } else if (whitePixelPurple > whitePixelTurquoise)
            {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 0, 250));
                Log.d(TAG, "Purple " + count);
                son = "purple";
            }
            else
            {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 250, 250));
                Log.d(TAG, "Turquoise " + count);
                son = "turquoise";
            }
        }
        if(begin)
        {
            if (son == father && father == grandpa && !algo)
            {
                Log.d(TAG, son);
                ausgabeAlt = ausgabe;
                ausgabe = son;
                algo = true;
                change = true;
                count++;
            } else if (son == father && father == grandpa && algo)
            {
                algo = false;
            } else if (son == father)
            {
                Log.d(TAG, son);
                count++;
                Log.d(TAG, "Count = " + (count ));
                ausgabeAlt = ausgabe;
                ausgabe = son;
                algo = true;
                change = true;
            }

            if (change)
            {
                if (ausgabeAlt == ausgabe)
                {
                    berCount++;
                    Log.d(TAG, "Count = " + count+ " berCount = " + berCount);
                }
                change = false;
            }
        }
    }

    public void polReturnToZeroDecOneBit() {
        grandpa = father;
        father = son;

        if (whitePixelRed < 188 && whitePixelGreen < 217 && whitePixelBlue < 308)// last Average values from test divided by 2
        {
            if (!weiss) {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 250, 250));
                blau = false;
                rot = false;
                gruen = false;
                weiss = true;
                son = "white";
                Log.d(TAG, "neutral");
            }
        } else {
            if (whitePixelRed > whitePixelGreen && whitePixelRed > whitePixelBlue) {
                if (!rot) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 0, 0));
                    blau = false;
                    rot = true;
                    gruen = false;
                    weiss = false;
                    son = "red";
                    bitString += "00";
                    Log.d(TAG, "00");
                }
            } else if (whitePixelBlue > whitePixelGreen) {
                if (!blau) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 0, 250));
                    blau = true;
                    rot = false;
                    gruen = false;
                    weiss = false;
                    son = "blue";
                    bitString += "0";
                    Log.d(TAG, "0");
                }
            } else {
                if (!gruen) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 250, 0));
                    blau = false;
                    rot = false;
                    gruen = false;
                    weiss = false;
                    son = "green";

                    Log.d(TAG, bitString);
                    Log.d(TAG, "BER = " + ((double) berCount / (double) bitString.length()) + " Errorbits = " + berCount + " Bits = " + bitString.length());

                    berCount = 0;
                    bitString = "";
                }
            }
        }
    }

    public void polReturnToZeroDecTwoBit()
    {
        grandpa = father;
        father = son;

        if (whitePixelRed < 188 && whitePixelGreen < 217 && whitePixelBlue < 308 && whitePixelYellow < 130 && whitePixelPurple < 190 && whitePixelTurquoise < 95)// last Average values from test divided by 2
        {
            if(!weiss) {
                image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 250, 250));
                blau = false;
                rot = false;
                gruen = false;
                weiss = true;
                gelb = false;
                lila = false;
                tuekis = false;
                son = "white";
                Log.d(TAG, "neutral");
            }
        } else {
            if (whitePixelRed > whitePixelGreen && whitePixelRed > whitePixelBlue&& whitePixelRed > whitePixelYellow && whitePixelRed > whitePixelPurple  && whitePixelRed > whitePixelTurquoise) {
                if (!rot) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 0, 0));
                    blau = false;
                    rot = true;
                    gruen = false;
                    weiss = false;
                    gelb = false;
                    lila = false;
                    tuekis = false;
                    son = "red";
                    bitString += "00";
                    Log.d(TAG, "00");
                }
            } else if (whitePixelGreen > whitePixelBlue && whitePixelGreen > whitePixelYellow && whitePixelGreen > whitePixelPurple && whitePixelGreen > whitePixelTurquoise) {
                if (!gruen) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 250, 0));
                    blau = false;
                    rot = false;
                    gruen = true;
                    weiss = false;
                    gelb = false;
                    lila = false;
                    tuekis = false;
                    son = "green";
                    bitString += "01";
                    Log.d(TAG, "01");
                }
            } else if (whitePixelBlue > whitePixelYellow && whitePixelBlue > whitePixelPurple && whitePixelBlue > whitePixelTurquoise){
                if (!blau) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 0, 250));
                    blau = true;
                    rot = false;
                    gruen = false;
                    weiss = false;
                    gelb = false;
                    lila = false;
                    tuekis = false;
                    son = "blue";
                    bitString += "10";
                    Log.d(TAG, "10");
                }
            } else if(whitePixelYellow > whitePixelPurple && whitePixelYellow > whitePixelTurquoise)
            {
                if (!gelb) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 250, 0));
                    blau = false;
                    rot = false;
                    gruen = false;
                    weiss = false;
                    gelb = true;
                    lila = false;
                    tuekis = false;
                    son = "yellow";
                    bitString += "11";
                    Log.d(TAG, "11");
                }
            } else if (whitePixelPurple > whitePixelTurquoise)
            {
                if (!lila) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(250, 0, 250));
                    blau = false;
                    rot = false;
                    gruen = false;
                    weiss = false;
                    gelb = false;
                    lila = true;
                    tuekis = false;
                    son = "purple";
                    /*for(int i = 0; i < bitString.length(); i++)
                    {
                        if(i%2 == 0)
                        {
                            if(bitString.charAt(i) != '1')
                            {
                                berCount ++;
                            }
                        }
                        else
                        {
                            if(bitString.charAt(i) != '0')
                            {
                                berCount ++;
                            }
                        }
                    }*/


                    Log.d(TAG,bitString);
                    Log.d(TAG, "BER = " + ((double)berCount/(double)bitString.length()) + " Errorbits = " + berCount + " Bits = " + bitString.length());

                    berCount = 0;
                    bitString = "";
                }
            }
            else
            {
                if(!tuekis) {
                    image = new Mat(rHeight, rWidth, mRgba.type(), new Scalar(0, 250, 250));
                    blau = false;
                    rot = false;
                    gruen = false;
                    weiss = false;
                    gelb = false;
                    lila = false;
                    tuekis = true;
                    son = "turquoise";
                    Log.d(TAG, "NOCH NIX");
                }
            }
        }
    }
}