package org.yhn.yq.client.view;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yhn.yq.R;
import org.yhn.yq.common.YQMessageType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RecentActivity extends Activity{
	ListView listView;
	List<RecentEntity> chatEntityList=new ArrayList<RecentEntity>();
	String[] mes;
	MyBroadcastReceiver br;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recent);
		 //注册广播
		IntentFilter myIntentFilter = new IntentFilter(); 
        myIntentFilter.addAction("org.yhn.yq.mes");
        br=new MyBroadcastReceiver();
        registerReceiver(br, myIntentFilter);
        
	    listView = (ListView) findViewById(R.id.lv_recent);
        listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//打开聊天页面
				Intent intent=new Intent(RecentActivity.this,ChatActivity.class);
				intent.putExtra("avatar", Integer.parseInt(mes[2]));
				intent.putExtra("account", Integer.parseInt(mes[0]));
				intent.putExtra("nick", mes[1]);
				startActivity(intent);
			}
        });
	}

	@Override
	public void finish() {
		 unregisterReceiver(br);
		super.finish();
	}

	//广播接收器
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			mes = intent.getStringArrayExtra("message");
		    Toast.makeText(context,mes[1]+" 对你说："+mes[3], Toast.LENGTH_SHORT).show();
		    //更新最近会话列表， 检测chatEntityList，防止同一个好友的消息出现多个会话实体
		    Iterator it=chatEntityList.iterator();
		    if(chatEntityList!=null && chatEntityList.size()!=0){
		    	while(it.hasNext()){
		    		RecentEntity re=(RecentEntity) it.next();
		    		if(re.getAccount()==Integer.parseInt(mes[0])){
		    			chatEntityList.remove(re);
		    		}
		    	}
		    }
		    int temp=Integer.parseInt(mes[2]);
		    if(mes[5].equals(YQMessageType.GROUP_MES)){
		    	temp=7;
		    }
		    chatEntityList.add(new RecentEntity(
		    		temp, 
		    		Integer.parseInt(mes[0]), 
		    		mes[1]+"", 
		    		mes[3],
		    		mes[4], 
		    		false));
		    listView.setAdapter(new RecentAdapter(RecentActivity.this, chatEntityList));
		}
	}
}
