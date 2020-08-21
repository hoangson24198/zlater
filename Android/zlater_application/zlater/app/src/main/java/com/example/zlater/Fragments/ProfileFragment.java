package com.example.zlater.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zlater.Activity.Login.LoginActivity;
import com.example.zlater.Adapter.RoutineAdapter;
import com.example.zlater.Helper.DateTimeHelper;
import com.example.zlater.Helper.OutputAnalyzer;
import com.example.zlater.Model.History;
import com.example.zlater.Model.Responses.HistoryResponse;
import com.example.zlater.Model.Responses.RoutineResponse;
import com.example.zlater.Model.Responses.UserResponse;
import com.example.zlater.Model.Routine;
import com.example.zlater.Model.User;
import com.example.zlater.Model.step.StepModel;
import com.example.zlater.Model.step.StepTransaction;
import com.example.zlater.Model.step.SuccessTransaction;
import com.example.zlater.R;
import com.example.zlater.Service.CameraService;
import com.example.zlater.Service.ForegroundServices;
import com.example.zlater.Service.local.ZlaterDatabase;
import com.example.zlater.Service.local.step.StepService;
import com.example.zlater.Service.local.step.StepThread;
import com.example.zlater.Service.remote.ZlaterService;
import com.example.zlater.Service.remote.RetrofitClient;
import com.example.zlater.Service.remote.RoutineAPI;
import com.example.zlater.Utils.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import at.grabner.circleprogress.CircleProgressView;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Integer maxValue;
    private LineChartView lineChart;
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    private CircleProgressView stepCount, calories, kilometer;
    private KProgressHUD progressDialog;
    private ZlaterService zlaterService;
    private TextView curr_bmi;
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;
    private OnFragmentInteractionListener mListener;
    AppCompatEditText edtHeight, edtWeight;
    Button btnUpdateBMI, btnSetTarger;
    RecyclerView viewHistory;
    RoutineAPI routineAPI;
    Switch on_off, foreground_model;
    EventBus bus;
    long numSteps;
    private boolean isServiceRun = false;
    private boolean isforeground_model = false;
    private View mLayout;
    private TextView logout;
    private EditText target;
    RealmAsyncTask realmAsyncTask;
    private StepThread thread;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private Button startMeasureHeartBeat;

    private final CameraService cameraService = new CameraService(this);

    private OutputAnalyzer analyzer;

    private SurfaceTexture previewSurfaceTexture;
    private  TextureView cameraTextureView;

    private Boolean state = true;

    public ProfileFragment() {
    }

    private Target fbPhotoContent = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            ShareHashtag hashtag = new ShareHashtag.Builder()
                    .setHashtag("#"+numSteps+"steps")
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .setShareHashtag(hashtag)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void initFacebook(){
        FacebookSdk.sdkInitialize(this.getContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Retrofit retrofit = RetrofitClient.getInstance();
        zlaterService = retrofit.create(ZlaterService.class);
        routineAPI=retrofit.create(RoutineAPI.class);
        connectView(view);
        initFacebook();
        sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", 0);
        on_off = (Switch) view.findViewById(R.id.on_off);
        foreground_model = (Switch) view.findViewById(R.id.foreground_model);

        thread = new StepThread(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("conf", MODE_PRIVATE);
        maxValue = sharedPreferences.getInt("maxValue", 6000);
        detectService();
        bus = EventBus.getDefault();
        bus.register(this);

        Realm realm = Realm.getDefaultInstance();
        StepModel result = realm.where(StepModel.class)
                .equalTo("date", DateTimeHelper.getToday())
                .findFirst();
        numSteps = result == null ? 0 : result.getNumSteps();
        bus.post(true);
        updateShowSteps();
        realm.close();
        drawChart();
        getAllRouting(userId);
        setUpCurrBmi(userId);
        return view;
    }

    public void drawChart() {

        // WeatherChartView mCharView = (WeatherChartView) findViewById(R.id.line_char);
        Date[] days = DateTimeHelper.get6days();
        Realm realm = Realm.getDefaultInstance();

        int[] data = new int[]{0, 0, 0, 0, 0, 0};
        int i = 0;
        for (Date d : days) {
            Log.d("eee","date "+d);
            if (i == 5) {
                data[i] = Integer.parseInt(String.valueOf(numSteps));
            }
            else {
                StepModel result = realm.where(StepModel.class)
                        .equalTo("date",d)
                        .findFirst();
                if (result != null) {
                    Log.d("eee","r !null  ");
                    data[i] = Integer.parseInt(String.valueOf(result.getNumSteps()));
                }
            }
            i++;
        }

        String[] xValues = DateTimeHelper.get6days(true);

        for (i = 0; i < xValues.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(xValues[i]));
        }

        for (i = 0; i < data.length; i++) {
            mPointValues.add(new PointValue(i, data[i]));
        }
        initLineChart();

    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#03A9F4"));
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.parseColor("#03A9F4"));

        axisX.setTextSize(10);
        axisX.setMaxLabelChars(8);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);

        axisX.setHasLines(true);


        Axis axisY = new Axis();

        axisY.setName("");

        axisY.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisYLeft(axisY);

        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icSettings:
                Log.e("Logout", "Logout" + sharedPreferences.getInt("id", 0));
                showPopupWindow(view);
                break;
            case R.id.btnUpdateBMI:
                if (btnUpdateBMI.getText().toString().equals(getText(R.string.need_update_bmi).toString())) {
                    btnUpdateBMI.setText(R.string.update_bmi);
                    enableFocus();
                } else if (btnUpdateBMI.getText().toString().equals(getText(R.string.update_bmi).toString())){
                    addHistory();
                }
                break;
            case R.id.stepCount:
                showPopupMaxValue(view);
                break;

            case R.id.startMeasureHeartBeat:
                if (state){
                    setStartMeasureHeartBeat();
                }else {
                    cameraService.stop();
                    if (analyzer != null ) analyzer.stop();
                    analyzer  = new OutputAnalyzer(this, getView().findViewById(R.id.graphTextureView));
                }
                break;
        }
    }

    private void setAnalyzer(){
        previewSurfaceTexture = null;
        previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if (previewSurfaceTexture != null) {
            // this first appears when we close the application and switch back - TextureView isn't quite ready at the first onResume.
            Surface previewSurface = new Surface(previewSurfaceTexture);

            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }

    private void setStartMeasureHeartBeat(){
        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        setAnalyzer();

    }

    private void setUpCurrBmi(int id) {
        Call<UserResponse> userResponseCall = zlaterService.getCurrentUser(id);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getStatus() == 0) {
                        User user = userResponse.getObject();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            curr_bmi.setText(String.valueOf(user.getBmi()));
                            edtHeight.setText(String.valueOf(user.getHeight()));
                            edtWeight.setText(String.valueOf(user.getWeight()));
                        } catch (Exception e) {
                            Log.e("err:", e + "");
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Có lỗi xảy ra. Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void addHistory() {
        showProgressDialog();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
        if (TextUtils.isEmpty(edtWeight.getText().toString())) {
            Toast.makeText(getContext(), "Vui lòng nhập cân nặng", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtHeight.getText().toString())) {
            Toast.makeText(getContext(), "Vui lòng nhập cân nặng", Toast.LENGTH_SHORT).show();
        } else {
            float bmi = Float.valueOf(edtWeight.getText().toString()) / (Float.valueOf(edtHeight.getText().toString()) * 2);
            History history = new History(bmi * 100, sharedPreferences.getInt("id", 0));
            Call<HistoryResponse> calledRegister = zlaterService.addHistory(history);
            calledRegister.enqueue(new Callback<HistoryResponse>() {
                @Override
                public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                    HistoryResponse historyResponse = response.body();
                    if (historyResponse.getStatus() == 0) {
                        Log.d("HS::", "Add history success");
                        updateUser(sharedPreferences.getInt("id", 0));

                    }
                }

                @Override
                public void onFailure(Call<HistoryResponse> call, Throwable t) {
                    progressDialog.dismiss();

                    Log.e("HS:::", "failed" + call.request() + ":::" + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean check = sharedPreferences.getBoolean("isTarget", true);
        if (check){
            if (buttonView.getId() == R.id.on_off) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("switch_on", isChecked);
                editor.apply();
                Intent intent = new Intent(this.getActivity(), StepService.class);

                if (isChecked) {
                    intent.putExtra("isActivity", false);
                    if (!bus.isRegistered(this))
                        bus.register(this);
                    Objects.requireNonNull(getActivity()).startService(intent);
                    bus.post(true);
                } else {
                    editor.putBoolean("foreground_model", isChecked);
                    editor.apply();
                    foreground_model.setChecked(false);
                    if (bus.isRegistered(this))
                        bus.unregister(this);
                    Objects.requireNonNull(getActivity()).stopService(intent);
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new StepTransaction(DateTimeHelper.getToday(), numSteps));
                    realm.close();
                }
            } else if (buttonView.getId() == R.id.foreground_model) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("foreground_model", isChecked);
                editor.apply();

                Intent intent = new Intent(this.getActivity(), StepService.class);
                if (isChecked) {
                    editor.putBoolean("switch_on", isChecked);
                    editor.apply();
                    on_off.setChecked(true);
                    intent.putExtra("foreground_model", "on");
                    intent.putExtra("isActivity", false);
                    if (!bus.isRegistered(this))
                        bus.register(this);
                    bus.post(true);
                } else {
                    intent.putExtra("foreground_model", "off");
                }
                Objects.requireNonNull(getActivity()).startService(intent);
            }
        }else{
            on_off.setChecked(false);
            foreground_model.setChecked(false);
            Toast.makeText(getActivity(),"Let's set your goals today",Toast.LENGTH_LONG).show();
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void connectView(View view) {
        ImageView icSetting = view.findViewById(R.id.icSettings);
        mLayout = view.findViewById(R.id.user_layout);
        lineChart = view.findViewById(R.id.line_chart);
        stepCount = view.findViewById(R.id.stepCount);
        startMeasureHeartBeat = view.findViewById(R.id.startMeasureHeartBeat);
        calories = view.findViewById(R.id.calories);
        kilometer = view.findViewById(R.id.kilometer);
        edtHeight = view.findViewById(R.id.edtHeight);
        edtWeight = view.findViewById(R.id.edtWeight);
        viewHistory=view.findViewById(R.id.viewHistory);
        curr_bmi = view.findViewById(R.id.curr_bmi);
        analyzer  = new OutputAnalyzer(this, view.findViewById(R.id.graphTextureView));
        cameraTextureView = view.findViewById(R.id.texttureCamera);
        disableFocus();
        btnUpdateBMI = view.findViewById(R.id.btnUpdateBMI);
        btnUpdateBMI.setOnClickListener(this);
        /*setUserInf();*/
        icSetting.setOnClickListener(this);
        stepCount.setOnClickListener(this);
        startMeasureHeartBeat.setOnClickListener(this);
    }

    private void showProgressDialog() {
        progressDialog = KProgressHUD.create(Objects.requireNonNull(getContext()))
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getText(R.string.please_wait).toString())
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    private void enableFocus() {
        edtHeight.setFocusable(true);
        edtHeight.setFocusableInTouchMode(true);
        edtWeight.setFocusable(true);
        edtWeight.setFocusableInTouchMode(true);
    }

    private void disableFocus() {
        edtHeight.setFocusable(false);
        edtHeight.requestFocus(1);
        edtHeight.setFocusableInTouchMode(false);
        edtWeight.setFocusable(false);
        edtWeight.setFocusableInTouchMode(false);
    }

    private void updateSharePref(float height, float weight) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(Constants.USER_INF, MODE_PRIVATE).edit();
        editor.putString("height", String.valueOf(height));
        editor.putString("weight", String.valueOf(weight));
        editor.apply();

    }

    private void updateUser(int user_id) {
        User user = new User(user_id, Float.valueOf(edtWeight.getText().toString()), Float.valueOf(edtHeight.getText().toString()));
        Call<UserResponse> callUpdate = zlaterService.updateUser(user);
        callUpdate.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                Log.e("HS:::", response.body() + " :: " + response.code() + "::" + userResponse.getMessage());

                if (userResponse.getStatus() == 0) {
                    Log.e("HS:::", "Update success");
                    updateSharePref(Float.valueOf(edtHeight.getText().toString()), Float.valueOf(edtWeight.getText().toString()));
                    btnUpdateBMI.setText(R.string.need_update_bmi);
                    setUpCurrBmi(user_id);
                    progressDialog.dismiss();
                    disableFocus();
                } else {
                    Log.e("HS:::", "Update failed");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                progressDialog.dismiss();

                Log.e("HS:::", "failed" + call.request() + ":::" + t.getMessage());
            }
        });
    }
    private void getAllRouting(Integer id) {

        Call<RoutineResponse> getRoutine = routineAPI.getRoutinesByUserId(id);
        getRoutine.enqueue(new Callback<RoutineResponse>() {
            @Override
            public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {
                RoutineResponse routineResponse = response.body();
                if (routineResponse.getStatus() == 0) {
                    viewHistory.setHasFixedSize(true);
                    viewHistory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    RoutineAdapter routineAdapter = new RoutineAdapter(removeDuplicate(routineResponse.getResponse()), getActivity());
                    viewHistory.setAdapter(routineAdapter);
                }
            }

            @Override
            public void onFailure(Call<RoutineResponse> call, Throwable t) {

            }
        });
    }

    private List<Routine> removeDuplicate(List<Routine> list) {
        Set<String> namesAlreadySeen = new HashSet<>();
        list.removeIf(routine -> !namesAlreadySeen.add(routine.getCreate_date()));
        Collections.reverse(list);
        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSteps(Long num) {
        numSteps = num;
        updateShowSteps();
    }

    private float calCalo(long steps){
        return (float) (0.05 * steps);
    }

    private float calKilometer(long steps) {
        return (float) (0.0008 * steps);
    }

    private void showAlertAchieve(){
        final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(getActivity())
                .setBackgroundColor(R.color.white)
                .setimageResource(R.drawable.cup)
                .setTextSubTitle("Congratulation!")
                .setBody(R.string.congratulation)
                .setPositiveButtonText("Share now")
                .setPositiveColor(R.color.colorPrimaryDark)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        Picasso.get().load(R.drawable.achievement).into(fbPhotoContent);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isTarget", false);
                        editor.apply();
                        SimpleDateFormat dateFormatSave = new SimpleDateFormat("yyyy-MM-dd");
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN,MODE_PRIVATE);
                        Routine routine=new Routine((int) numSteps,dateFormatSave.format(DateTimeHelper.getToday()),null,String.valueOf(kilometer.getMaxValue()),String.valueOf(calories.getMaxValue()),sharedPreferences.getInt("id",0));
                        ZlaterDatabase.getInstance(getApplicationContext()).routineDAO().insert(routine);
                        dialog.dismiss();
                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(false)
                .build();
        alert.show();
        int userId = sharedPreferences.getInt("id", 0);
        getAllRouting(userId);
    }

    private void handleMaxValue(){
        int max = sharedPreferences.getInt("maxValue", 6000);
        if (numSteps == max && on_off != null){
            Log.e("HSHandleMaxValue", "maxValue:" + max);
            on_off.setChecked(false);
            showAlertAchieve();
        }else{
            Log.e("HSHandleMaxValue", "value"+numSteps);
        }
    }

    private void updateShowSteps() {
        stepCount.setValue(numSteps);
        stepCount.setText(""+numSteps);
        stepCount.setMaxValue(maxValue);
        stepCount.setUnit("/" + maxValue + " steps");

        calories.setMaxValue(calCalo((long) stepCount.getMaxValue()));
        calories.setText(""+calCalo(numSteps));
        calories.setValue(calCalo(numSteps));

        kilometer.setMaxValue(calKilometer((long) stepCount.getMaxValue()));
        kilometer.setText(""+calKilometer(numSteps));
        kilometer.setValue(calKilometer(numSteps));

        lineChart.refreshDrawableState();

        handleMaxValue();
    }

    private void showPopupWindow(View view) {

        ForegroundServices app = (ForegroundServices) getApplicationContext();
        isServiceRun=app.getServiceRun();

        isforeground_model=sharedPreferences.getBoolean("foreground_model",false);

        View contentView = getLayoutInflater().inflate(R.layout.settings_layout, null, false);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        on_off = contentView.findViewById(R.id.on_off);
        foreground_model = contentView.findViewById(R.id.foreground_model);
        logout = contentView.findViewById(R.id.logout);
        on_off.setChecked(isServiceRun);
        foreground_model.setChecked(isforeground_model);

        on_off.setOnCheckedChangeListener(this);
        foreground_model.setOnCheckedChangeListener(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser(sharedPreferences.getInt("id", 0));
            }
        });

        mBuilder.setView(contentView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    private void showPopupMaxValue(View view) {

        View contentView = getLayoutInflater().inflate(R.layout.set_target_layout, null, false);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        target = contentView.findViewById(R.id.edt_target);
        btnSetTarger = contentView.findViewById(R.id.btn_submit);
        mBuilder.setView(contentView);
        AlertDialog dialogMax = mBuilder.create();
        dialogMax.show();

        btnSetTarger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (target.getText() != null){
                    Log.e("HStarger", "onClick: " + target.getText().toString() );
                    maxValue = Integer.valueOf(target.getText().toString());
                    stepCount.setUnit("/" + maxValue + " steps");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("maxValue", maxValue);
                    editor.putBoolean("isTarget",true);
                    editor.apply();
                    dialogMax.dismiss();
                }
            }
        });
    }

    private void detectService() {
        ForegroundServices app = (ForegroundServices) getApplicationContext();
        isServiceRun = app.getServiceRun();
        boolean temp = sharedPreferences.getBoolean("switch_on", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isServiceRun != temp) {
            if (!isServiceRun) {
                Toast.makeText(getApplicationContext(), "The step counting service terminated unexpectedly, please whitelist the application",
                        Toast.LENGTH_LONG).show();
            }
            editor.putBoolean("switch_on", isServiceRun);
            editor.apply();
        }

        temp = sharedPreferences.getBoolean("foreground_model", false);
        if (temp && !isServiceRun) {
            editor.putBoolean("foreground_model", false);
            editor.apply();
            isforeground_model = false;
        } else isforeground_model = temp;
    }

    //User logout
    private void logoutUser(Integer userId) {
        showProgressDialog();
        Call<UserResponse> callLogout = zlaterService.userLogout(userId);
        callLogout.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor sharedPreferences = getActivity().getSharedPreferences(Constants.LOGIN, MODE_PRIVATE).edit();
                    sharedPreferences.putString("username", "");
                    sharedPreferences.putString("password", "");
                    sharedPreferences.apply();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
                if (!response.isSuccessful()) {
                    Log.e("HS:::", response.code() + "");
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Logout", "Failed" + call.toString());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("eee", "activity stop()");
        bus.post(false);
        if (bus.isRegistered(this))
            bus.unregister(this);
    }
}
