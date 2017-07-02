package org.yhn.yq.client.view;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.yhn.yq.R;
import org.yhn.yq.client.model.ManageClientConServer;
import org.yhn.yq.client.model.SendMessage;
import org.yhn.yq.common.YQMessage;
import org.yhn.yq.common.YQMessageType;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BuddyActivity extends Activity {
	ListView listView;
	public static String buddyStr="";
	public static String buddyStr3="";
	List<BuddyEntity> buddyEntityList = new ArrayList<BuddyEntity>();//好友列表
	BuddyAdapter ba;//好友列表的adapter
	BuddyAdapter ma;
	BuddyEntity temp;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_buddy);
		
		//ImageView avatarIv=(ImageView) findViewById(R.id.buddy_top_avatar);
		//TextView nickTv=(TextView) findViewById(R.id.buddy_top_nick);
		
		//nickTv.setText(MoreActivity.me.getNick());
	    //avatarIv.setImageResource(ChatActivity.avatar[MoreActivity.me.getAvatar()]);
	    IntentFilter intentFilter = new IntentFilter();  
	    intentFilter.addAction("action.refreshFriend");  
	    registerReceiver(mRefreshBroadcastReceiver, intentFilter);  
	    
	    IntentFilter intentFilter2 = new IntentFilter();  
	    intentFilter2.addAction("action.getbuddy");  
	    registerReceiver(mRefreshBroadcastReceiver2, intentFilter2);  
	    
		listView = (ListView) findViewById(R.id.listview);
        ////填充数据
		ba=new BuddyAdapter(this,jieXi(buddyStr));
        listView.setAdapter(ba);
        setListViewListener();
        
	}
	  

	
	private void setListViewListener() {
		listView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> a, View v, int position,long l) {
				temp= (BuddyEntity) listView.getItemAtPosition(position);
				//打开聊天页面
				Intent intent=new Intent(BuddyActivity.this,ChatActivity.class);
				intent.putExtra("avatar", temp.getAvatar());
				intent.putExtra("account",temp.getAccount());
				intent.putExtra("nick", temp.getNick());
				startActivity(intent);
			}
        });
        listView.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				temp= (BuddyEntity) listView.getItemAtPosition(position);
				listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
					public void onCreateContextMenu(ContextMenu menu,
							View arg1, ContextMenuInfo arg2) {
						menu.setHeaderTitle("操作");
						menu.add(0,0,0,"发起会话");
						menu.add(0,1,0,"删除好友");
						menu.add(0,2,0,"查看好友资料");
					}
				});
				return false;
			}
        });
	}
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 0:
			//打开聊天页面
			Intent intent=new Intent(BuddyActivity.this,ChatActivity.class);
			intent.putExtra("avatar", temp.getAvatar());
			intent.putExtra("account",temp.getAccount());
			intent.putExtra("nick", temp.getNick());
			startActivity(intent);
			break;
		case 1:
			//向服务器发送一个删除好友的包
			SendMessage.sendADbuddy(MoreActivity.me.getAccount(), 
					temp.getAccount(), 
					YQMessageType.DEL_BUDDY);
			//删除好友列表中的该好友
			for(int i=0;i<buddyEntityList.size();i++){
				if((buddyEntityList.get(i).getAccount())==temp.getAccount()){
					buddyEntityList.remove(i);
				}
			}
			listView = (ListView) findViewById(R.id.listview);
			ba=new BuddyAdapter(this,buddyEntityList);
	        listView.setAdapter(ba);
			break;
		case 2:
			//
			break;
		}
		return super.onContextItemSelected(item);
	}

	private List<BuddyEntity> jieXi(String s){
        String ss[] = buddyStr.split(" ");
        for(String a: ss){
        	if(a!=""){
	        	String b[]=a.split("_");
	            buddyEntityList.add(new BuddyEntity(
	            		Integer.parseInt(b[2]), 
	            		Integer.parseInt(b[0]), 
	            		b[1], 
	            		b[3],
	            		Integer.parseInt(b[4])));
        	}
        }
		return buddyEntityList;
	}
	private List<BuddyEntity> jieXi2(String s){
		buddyEntityList.clear();
        String ss[] = s.split(" ");
        for(String a: ss){
        	if(a!=""){
	        	String b[]=a.split("_");
	            buddyEntityList.add(new BuddyEntity(
	            		Integer.parseInt(b[2]), 
	            		Integer.parseInt(b[0]), 
	            		b[1], 
	            		b[3],
	            		Integer.parseInt(b[4])));
        	}
        }
		return buddyEntityList;
	}
	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {  
	      @Override  
	      public void onReceive(Context context, Intent intent) {  
	          String action = intent.getAction();  
	          if (action.equals("action.refreshFriend"))  
	          { 
	        	  //String buddy = intent.getStringExtra("Buddy");
	        	  //Toast.makeText(BuddyActivity.this, buddy, Toast.LENGTH_LONG).show();
	        	  try {
						//发送一个要求返回在线好友的请求的Message
						ObjectOutputStream oos2 = new ObjectOutputStream	(
								ManageClientConServer.getClientConServerThread(MoreActivity.me.getAccount()).getS().getOutputStream());
						YQMessage m=new YQMessage();
						m.setType(YQMessageType.REFRESH_BUDDY);
						m.setSender(MoreActivity.me.getAccount());
						oos2.writeObject(m);
					} catch (IOException e) {
						e.printStackTrace();
					}
			   /** Toast.makeText(BuddyActivity.this, buddyStr3, Toast.LENGTH_LONG).show();
	        	listView = (ListView) findViewById(R.id.listview);
	    		ma=new BuddyAdapter(BuddyActivity.this,jieXi(buddyStr3));
	  	        listView.setAdapter(ma);	   **/       }  
	      }  
	  };  
	  private BroadcastReceiver mRefreshBroadcastReceiver2 = new BroadcastReceiver() {  
	      @Override  
	      public void onReceive(Context context, Intent intent) {  
	          String action = intent.getAction();  
	          if (action.equals("action.getbuddy"))  
	          { 
	        	  Log.i("","已经接收到了这个intent");
	        	  String buddy = intent.getStringExtra("Buddy");
	        	 // Toast.makeText(BuddyActivity.this, buddy, Toast.LENGTH_LONG).show();
	        	  Log.i("", "--"+buddy);
	      		  ma=new BuddyAdapter(BuddyActivity.this,jieXi2(buddy));
	              listView.setAdapter(ma);
	              //setListViewListener();
			    //Toast.makeText(BuddyActivity.this, buddyStr3, Toast.LENGTH_LONG).show();
	        	          }  
	      }  
	  };  
	  @Override  
	   protected void onDestroy() {  
	       super.onDestroy();  
	       unregisterReceiver(mRefreshBroadcastReceiver); 
	       unregisterReceiver(mRefreshBroadcastReceiver2);  

	   }
}
