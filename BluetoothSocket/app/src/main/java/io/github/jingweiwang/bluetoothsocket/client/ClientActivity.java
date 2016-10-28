package io.github.jingweiwang.bluetoothsocket.client;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.jingweiwang.bluetoothsocket.BluetoothUtils;
import io.github.jingweiwang.bluetoothsocket.R;

public class ClientActivity extends AppCompatActivity implements BluetoothUtils.SocketCallback {
    @BindView(R.id.btn_serch)
    Button btn_serch;
    @BindView(R.id.rv_btlist)
    RecyclerView rv_btlist;
    @BindView(R.id.tv_connect)
    TextView tv_connect;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.et_send)
    EditText et_send;
    private List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private String connected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);
        BluetoothUtils.init(this);
        BluetoothUtils.getInstance().setSocketCallback(this);
    }

    @Override
    protected void onDestroy() {
        BluetoothUtils.getInstance().stopDiscovery();
        BluetoothUtils.getInstance().unregisterPairingReceiver();
        BluetoothUtils.getInstance().shutdownClient();
        super.onDestroy();
    }

    @OnClick(R.id.btn_serch)
    public void serchBluetooth() {
        tv_connect.setText("正在扫描蓝牙设备");
        BluetoothUtils.getInstance().openBluetooth();
        BluetoothUtils.getInstance().shutdownClient();
        bluetoothDeviceList.clear();
        rv_btlist.setVisibility(View.VISIBLE);
        btn_send.setVisibility(View.GONE);
        et_send.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!BluetoothUtils.getInstance().getBluetoothAdapter().isEnabled()) {
                    Log.e("BluetoothAdapter", "sleep");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("BluetoothAdapter", "isEnabled");
                BluetoothUtils.getInstance().discovery();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv_btlist.setLayoutManager(new LinearLayoutManager(ClientActivity.this));
                    }
                });
                final BTListAdapter btListAdapter = new BTListAdapter();
                for (int i = 0; i < 60; i++) {
                    bluetoothDeviceList = BluetoothUtils.getInstance().getBluetoothDeviceList();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv_btlist.setAdapter(btListAdapter);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @OnClick(R.id.btn_send)
    public void sendText() {
        String text = et_send.getText().toString();
        BluetoothUtils.getInstance().sendMessageHandle(text);
    }

    @Override
    public void onSocketCallback(int result) {
        switch (result) {
            case BluetoothUtils.STATUS_CONNECTING:
                Toast.makeText(this, "STATUS_CONNECTING", Toast.LENGTH_SHORT).show();
                tv_connect.setText("正在建立连接");
                break;
            case BluetoothUtils.STATUS_CONNECTED:
                BluetoothUtils.getInstance().unregisterPairingReceiver();
                rv_btlist.setVisibility(View.GONE);
                tv_connect.setText("与" + connected + "建立连接");
                btn_send.setVisibility(View.VISIBLE);
                et_send.setVisibility(View.VISIBLE);
                Toast.makeText(this, "STATUS_CONNECTED", Toast.LENGTH_SHORT).show();
                break;
            case BluetoothUtils.STATUS_CONNECT_ERROR:
                tv_connect.setText("连接错误请重试");
                Toast.makeText(this, "STATUS_CONNECT_ERROR", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    class BTListAdapter extends RecyclerView.Adapter<BTListAdapter.BTListHolder> {

        @Override
        public BTListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(ClientActivity.this, R.layout.item_btlist, null);
            BTListHolder btListHolder = new BTListHolder(view);
            return btListHolder;
        }

        @Override
        public void onBindViewHolder(final BTListHolder holder, int position) {
            holder.bt_name.setText(bluetoothDeviceList.get(position).getName());
            holder.bt_mac.setText(bluetoothDeviceList.get(position).getAddress());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("onClick", bluetoothDeviceList.get(holder.getAdapterPosition()).getName());
                    BluetoothUtils.getInstance().stopDiscovery();
                    BluetoothUtils.getInstance().pairing();
                    BluetoothUtils.getInstance().startClientThread(bluetoothDeviceList.get(holder.getAdapterPosition()));
                    connected = bluetoothDeviceList.get(holder.getAdapterPosition()).getName() + " " + bluetoothDeviceList.get(holder.getAdapterPosition()).getAddress();
                }
            });
        }

        @Override
        public int getItemCount() {
            return bluetoothDeviceList.size();
        }


        class BTListHolder extends RecyclerView.ViewHolder {
            public TextView bt_name, bt_mac;

            public BTListHolder(View itemView) {
                super(itemView);
                bt_name = (TextView) itemView.findViewById(R.id.bt_name);
                bt_mac = (TextView) itemView.findViewById(R.id.bt_mac);
            }
        }
    }
}
