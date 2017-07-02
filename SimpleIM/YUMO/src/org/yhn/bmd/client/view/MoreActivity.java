package org.yhn.bmd.client.view;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.bmd.yq.R;
import org.bmd.yq.client.model.ManageClientConServer;
import org.bmd.yq.client.model.SendMessage;
import org.yhn.bmd.common.MyTime;
import org.yhn.bmd.common.User;
import org.yhn.bmd.common.YQMessage;
import org.yhn.bmd.common.YQMessageType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity{
	public static User me; //当前账号的个人资料
	//因为在别的类中有用到me，所以提前解析了个人资料，
	public static String buddyStr2="";
	static{
		me=jieXi(MainActivity.myInfo);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
		
		ImageView avatar=(ImageView) findViewById(R.id.avatar_more);
		TextView nick=(TextView) findViewById(R.id.nick_more);
		TextView sex=(TextView) findViewById(R.id.sex_more);
		TextView age=(TextView) findViewById(R.id.age_more);
		TextView trends=(TextView) findViewById(R.id.trends_more);
		
		avatar.setImageResource(ChatActivity.avatar[me.getAvatar()]);
		nick.setText(me.getNick());
		sex.setText(me.getSex());
		age.setText(me.getAge()+"岁");
		trends.setText(me.getTrends());
		
		findViewById(R.id.imageView2).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
			 	 
			 	LayoutInflater factory = LayoutInflater.from(MoreActivity.this);
			 	 //得到自定义对话框
			 	 final View DialogView = factory.inflate(R.layout.dialog, null);
			 	 //创建对话框
			 	 AlertDialog dlg = new AlertDialog.Builder(MoreActivity.this)
			 	     .setTitle("请输入你要添加的好友的账号")
			 	     .setView(DialogView)//设置自定义对话框的样式
			 	     .setPositiveButton("确定", //设置"确定"按钮
			 	         new DialogInterface.OnClickListener() //设置事件监听
			 	         {
			 	             public void onClick(DialogInterface dialog, int whichButton) {
			 	                //输入后点击“确定”，开始获取我们要的内容 DialogView就是AlertDialog弹出的Activity
			 	                 EditText edtUserName = (EditText)DialogView.findViewById(R.id.username);
			 	                 String strUserName = edtUserName.getText().toString();
			 					 SendMessage.sendADbuddy(MoreActivity.me.getAccount(), 
			 							Integer.parseInt(strUserName), 
			 							YQMessageType.ADD_BUDDY);
			 					
			 		/**			try {
									//发送一个要求返回在线好友的请求的Message
									ObjectOutputStream oos2 = new ObjectOutputStream	(
											ManageClientConServer.getClientConServerThread(me.getAccount()).getS().getOutputStream());
									YQMessage m=new YQMessage();
									m.setType(YQMessageType.REFRESH_BUDDY);
									m.setSender(me.getAccount());
									oos2.writeObject(m);
									//BuddyActivity.**/
									//ListView listView = (ListView) findViewById(R.id.listview);
							        ////填充数据
									//ba=new BuddyAdapter(BuddyActivity.this,jieXi(buddyStr));
							        //listView.setAdapter(ba);
									 //Toast.makeText(MoreActivity.this, buddyStr2, Toast.LENGTH_LONG).show();
							         Intent intent = new Intent();  
							         intent.setAction("action.refreshFriend");  
							         sendBroadcast(intent);  
							//	} catch (IOException e) {
								//	e.printStackTrace();
								//}
			 	             }
			 	         })
			 	     .setNegativeButton("取消", //设置“取消”按钮
			 	         new DialogInterface.OnClickListener()
			 	         {
			 	             public void onClick(DialogInterface dialog, int whichButton) {
			 	             }
			 	         })
			 	     .create();//创建弹出框
			 	 dlg.show();//显示
			 	 
			}
		});
		
	}

	private static User jieXi(String str) {
		User user=new User();
		String s[] = str.split("_");
    	if(s!=null){
	        user.setAccount(Integer.parseInt(s[0]));
	        user.setNick(s[1]);
	        user.setAvatar(Integer.parseInt(s[2]));
	        user.setTrends(s[3]);
	        user.setSex(s[4]);
	        user.setAge(Integer.parseInt(s[5]));
	        user.setLev(Integer.parseInt(s[6]));
    	}
		return user;
	}
}
