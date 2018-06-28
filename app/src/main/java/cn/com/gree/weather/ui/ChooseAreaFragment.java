package cn.com.gree.weather.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.gree.weather.R;
import cn.com.gree.weather.adapter.OnRvItemClickListener;
import cn.com.gree.weather.adapter.PositionAdapter;
import cn.com.gree.weather.base.WeatherApplication;
import cn.com.gree.weather.db.City;
import cn.com.gree.weather.db.County;
import cn.com.gree.weather.db.Province;
import cn.com.gree.weather.util.HttpUtil;
import cn.com.gree.weather.util.LogUtil;
import cn.com.gree.weather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Author:liuyanguo
 * Date:2018/6/20
 * Time:9:37
 * Description:
 */
public class ChooseAreaFragment extends Fragment implements OnRvItemClickListener {
    private static final String TAG = ChooseAreaFragment.class.getSimpleName();

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private RelativeLayout mRlTitleLayout;
    private ProgressDialog mProgressDialog;
    private TextView mTitleText;
    private Button mBackButton;
    private RecyclerView mRecyclerView;
    private PositionAdapter mAdapter;
    private List<String> mDataList = new ArrayList<>();

    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    private BDLocation mBDLocation;
    private StringBuilder mCurrLatiLong, mCurrDetail, mCurrLocMethod;

    /**
     * 省列表
     */
    private List<Province> mProvinceList;

    /**
     * 市列表
     */
    private List<City> mCityList;

    /**
     * 县列表
     */
    private List<County> mCountyList;

    /**
     * 选中的省份
     */
    private Province mSelectedProvince;

    /**
     * 选中的城市
     */
    private City mSelectedCity;

    /**
     * 当前选中的级别
     */
    private int mCurrentLevel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_area, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTitleText = view.findViewById(R.id.title_text);
        mBackButton = view.findViewById(R.id.back_button);

        initView(view);
        initRecyclerView(view);
        initLocationClient();
        requestRuntimePermission();
    }

    private void initView(View view) {
        mRlTitleLayout = view.findViewById(R.id.title_layout);
    }

    private void initRecyclerView(@NonNull View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4,
                LinearLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new PositionAdapter(getActivity(), mDataList);
        mAdapter.setOnRvItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 请求运行时权限
     */
    private void requestRuntimePermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }
    }

    /**
     * 开始请求定位
     */
    private void requestLocation() {
        mLocationClient.start();
    }

    /**
     * 初始化LocationClient的参数
     */
    private void initLocationClient() {
        mLocationClient = new LocationClient(WeatherApplication.getContext());
        mOption = new LocationClientOption();
        mOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(mOption);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {
                mBDLocation = bdLocation;
                //从bdLocation中获取位置信息
                mCurrLatiLong = new StringBuilder();
                mCurrLatiLong.append("纬度：").append(bdLocation.getLatitude())
                        .append("\n");
                mCurrLatiLong.append("经度：").append(bdLocation.getLongitude())
                        .append("\n");
                Log.e(TAG, "经纬度" + "\n" + mCurrLatiLong.toString());
                mCurrDetail = new StringBuilder();
                mCurrDetail.append("国家：").append(bdLocation.getLongitude())
                        .append("\n");
                mCurrDetail.append("省：").append(bdLocation.getProvince())
                        .append("\n");
                mCurrDetail.append("市：").append(bdLocation.getCity())
                        .append("\n");
                mCurrDetail.append("区：").append(bdLocation.getDistrict())
                        .append("\n");
                mCurrDetail.append("街道：").append(bdLocation.getStreet())
                        .append("\n");
                Log.e(TAG, "详细信息" + "\n" + mCurrDetail.toString());

                mCurrLocMethod = new StringBuilder();
                mCurrLocMethod.append("定位方式：");
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    mCurrLocMethod.append("GPS");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    mCurrLocMethod.append("网络");
                }
                Log.e(TAG, "定位" + "\n" + mCurrLocMethod.toString());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (mCurrentLevel == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    @Override
    public void onItemClick(int position) {
        if (mCurrentLevel == LEVEL_PROVINCE) {
            mSelectedProvince = mProvinceList.get(position);
            queryCities();
        } else if (mCurrentLevel == LEVEL_CITY) {
            mSelectedCity = mCityList.get(position);
            queryCounties();
        } else if (mCurrentLevel == LEVEL_COUNTY) {
            String weatherId = mCountyList.get(position).getWeatherId();
            if (getActivity() instanceof MainActivity) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                intent.putExtra("weather_id", weatherId);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_to_left, 0);
                getActivity().finish();
            } else if (getActivity() instanceof WeatherActivity) {
                WeatherActivity activity = (WeatherActivity) getActivity();
                activity.mDrawerLayout.closeDrawers();
                activity.mSwipeRefresh.setRefreshing(true);
                activity.requestWeather(weatherId);
            }
        }
    }

    @Override
    public void onItemClick(Object object) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvince() {
        mTitleText.setText("中国");
        mBackButton.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size() > 0) {
            mDataList.clear();
            for (Province province : mProvinceList) {
                mDataList.add(province.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
//            mListView.setSelection(0);
            mRecyclerView.scrollToPosition(0);
            mCurrentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询选中市内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        mTitleText.setText(mSelectedProvince.getProvinceName());
        mBackButton.setVisibility(View.VISIBLE);
        mCityList = DataSupport.where("provinceid = ?", String.valueOf(mSelectedProvince.getId())).find(City.class);
        if (mCityList.size() > 0) {
            mDataList.clear();
            for (City city : mCityList) {
                mDataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
//            mListView.setSelection(0);
            mRecyclerView.scrollToPosition(0);
            mCurrentLevel = LEVEL_CITY;
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询选中省内所有的县，有限从数据库查询，如果没有查询再去服务器上查询
     */
    private void queryCounties() {
        mTitleText.setText(mSelectedCity.getCityName());
        mBackButton.setVisibility(View.VISIBLE);
        mCountyList = DataSupport.where("cityid = ?", String.valueOf(mSelectedCity.getId())).find(County.class);
        if (mCountyList.size() > 0) {
            mDataList.clear();
            for (County county : mCountyList) {
                mDataList.add(county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
//            mListView.setSelection(0);
            mRecyclerView.scrollToPosition(0);
            mCurrentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(WeatherApplication.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.d(TAG, "response = " + responseText);
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, mSelectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, mSelectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

    public RelativeLayout getTitleLayout() {
        return mRlTitleLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
