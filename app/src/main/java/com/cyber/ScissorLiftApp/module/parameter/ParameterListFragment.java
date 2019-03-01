package com.cyber.ScissorLiftApp.module.parameter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.adapter.Register;
import com.cyber.ScissorLiftApp.module.base.BaseListFragment;
import com.cyber.ScissorLiftApp.module.bean.LoadingBean;
import com.cyber.ScissorLiftApp.module.bean.LoadingEndBean;
import com.cyber.ScissorLiftApp.util.DiffCallback;
import com.cyber.ScissorLiftApp.util.RxBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


public class ParameterListFragment extends BaseListFragment<IParameterList.Presenter> implements IParameterList.View {
    private static final String JSON_LIST = "jsonList";
    private static final String TITLE = "title";
    public static final String TAG = "ParameterListFragment";
    private String jsonList;
    private String title;
    public boolean isEditStatus = false;
    private Menu menu;
    Observable<String> observable;
    public static ParameterListFragment newInstance(int jsonList,String title) {
        ParameterListFragment instance = new ParameterListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(JSON_LIST, jsonList);
        bundle.putString(TITLE,title);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list_toolbar;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //观察者查看
        observable = RxBus.getInstance().register(ParameterListFragment.TAG);
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        int jsonListId = 0;
        if (arguments != null) {
            jsonListId = arguments.getInt(JSON_LIST);
        }
        if(jsonListId == -1) jsonList = "";
        else jsonList = getContext().getResources().getString(jsonListId);
        title = arguments.getString(TITLE);
        onLoadData("0");
        setMenuShow();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        initToolBar(toolbar, true, title);
        toolbar.setOnClickListener(view1 -> recyclerView.smoothScrollToPosition(0));
//        toolbar.setBackgroundColor(SettingUtil.getInstance().getColor());

        adapter = new MultiTypeAdapter(oldItems);
        //注册muiltitype的item viewholder
        Register.registerParameterListItem(adapter,oldItems);
        recyclerView.setAdapter(adapter);
         /* 如果需要划到底部加载更多数据则不需注释该块
        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    canLoadMore = false;
                    presenter.doLoadMoreData();
                }
            }
        });
        */
        setHasOptionsMenu(true);
    }

    @Override
    public void onRefresh() {
        if(!isEditStatus)
        presenter.doRefresh();
    }

    /**
     * @param arg
     * arg[0] 0:只读模式 1:编辑模式
     * arg[1] if exist:编辑模式下list定位到第arg[1]个item
     */
    @Override
    public void onLoadData(String...arg) {
        if("0".equals(arg[0])){
            onShowLoading();
            presenter.doLoadData(jsonList,"0");
        } else if ("1".equals(arg[0])) {
            presenter.doLoadData(jsonList,"1");
            if (arg.length==2)
            recyclerView.smoothScrollToPosition(Integer.parseInt(arg[1]));
        }
    }

    @Override
    public void onSetAdapter(final List<?> list) {
        Items newItems = new Items(list);
        newItems.add(new LoadingEndBean());
        //4.adapter更新recyclerview
        DiffCallback.create(oldItems, newItems, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        /*
        presenter如果是本地改变了数据后,由于是浅拷贝,olditems和newitems数据集在持有的引用指向同一个位置,所以在此处不能更新items
        所以需要调用notifyDataSetChanged()
         */
        adapter.notifyDataSetChanged();
        // 设置完成适配器后则恢复canloadmore
//        canLoadMore = true;
//        recyclerView.stopScroll();
    }

    @Override
    public void setPresenter(IParameterList.Presenter presenter) {
        if (null == presenter) {
            this.presenter = new ParameterListPresenter(this);
        }

    }

    @Override
    public void fetchData() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_parameter_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (this.menu == null) {
            this.menu = menu;
        }
        setMenuShow();
        super.onPrepareOptionsMenu(menu);
    }

    void setMenuShow() {
        if (isEditStatus) {
            menu.findItem(R.id.action_share).setVisible(false);
            menu.findItem(R.id.action_finish).setVisible(true);
            menu.findItem(R.id.action_goto_edit_mode).setVisible(false);
        } else {
            menu.findItem(R.id.action_share).setVisible(true);
            menu.findItem(R.id.action_goto_edit_mode).setVisible(presenter.isEditable());
            menu.findItem(R.id.action_finish).setVisible(false);
        }
    }
    void gotoEditMode() {
        isEditStatus = true;
        setMenuShow();//设置右上角toolbar按键
        swipeRefreshLayout.setOnRefreshListener(this::onHideLoading);
    }
    void exitEditMode() {
        isEditStatus = false;
        setMenuShow();
        swipeRefreshLayout.setOnRefreshListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * 分享该app
             */
            case R.id.action_share:
                Toast.makeText(getContext(), "share", Toast.LENGTH_LONG).show();
                break;
            /*
              进入编辑模式
             */
            case R.id.action_goto_edit_mode:
                gotoEditMode();
                onLoadData("1");
                Log.d(TAG, "onOptionsItemSelected: goto edit mode");
                //1.接收到修改的信息
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            String[] args = s.split(" ");
                            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onOptionsItemSelected: "+ParameterListFragment.TAG+": "+s);
                            //2.presenter修改数据
                            presenter.doLoadData(jsonList,"1",args);
                        });
                break;
            /*
             * 编辑完成,检测蓝牙通讯及设置是否成功,做出下一步反馈
             */
            case R.id.action_finish:
                //TODO 蓝牙通讯并检测是否成功
                exitEditMode();
                onLoadData("0");
                Log.d(TAG, "onOptionsItemSelected: action_finish"+adapter.getItems());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
