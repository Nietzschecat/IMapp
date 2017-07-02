package org.yhn.bmd.client.view;
import java.util.ArrayList;
import java.util.List;

import org.bmd.yq.R;
import org.bmd.yq.client.model.SendMessage;
import org.yhn.bmd.common.MyTime;
import org.yhn.bmd.common.YQMessageType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Activity {
	EditText et_input;
	private String chatContent;//��Ϣ����
	ListView chatListView;
	public List<ChatEntity> chatEntityList=new ArrayList<ChatEntity>();//������������
	private int chatAccount;
	private String chatNick;
	public static int[] avatar=new int[]{R.drawable.avatar_default,R.drawable.h001,R.drawable.h002,R.drawable.h003,
			R.drawable.h004,R.drawable.h005,R.drawable.h006,R.drawable.group_avatar};
	MyBroadcastReceiver br;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		//����top�����Ϣ
		int chatAvatar=getIntent().getIntExtra("avatar", 0);
		chatAccount=getIntent().getIntExtra("account", 0);
		chatNick=getIntent().getStringExtra("nick");
		//ImageView avatar_iv=(ImageView) findViewById(R.id.chat_top_avatar);
		//avatar_iv.setImageResource(avatar[chatAvatar]);
		TextView nick_tv=(TextView) findViewById(R.id.chat_top_nick);
		nick_tv.setText(chatNick);
		
		findViewById(R.id.ib_send).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				//�õ���������ݣ������EditText
				et_input=(EditText) findViewById(R.id.et_input);
				chatContent=et_input.getText().toString();
				et_input.setText("");
				//������������
				updateChatView(new ChatEntity(
						MoreActivity.me.getAvatar(),
						MoreActivity.me.getNick(),
						chatContent,
			    		MyTime.geTime(),
			    		false));
				//������Ϣ
				if(getIntent().getIntExtra("avatar", 0)==7){ //Ⱥ��Ϣ
					SendMessage.sendMes(chatAccount, chatContent,YQMessageType.GROUP_MES);
				}else { //������Ϣ
					SendMessage.sendMes(chatAccount, chatContent,YQMessageType.COM_MES);
				}
			}
		});
		 //ע��㲥
		IntentFilter myIntentFilter = new IntentFilter(); 
        myIntentFilter.addAction("org.yhn.yq.mes");
        br=new MyBroadcastReceiver();
        registerReceiver(br, myIntentFilter);
		ManageActivity.addActiviy("ChatActivity", this);
	}
	@Override
	public void finish() {
		 unregisterReceiver(br);
		super.finish();
	}
	
	//�㲥������
	public class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String[] mes = intent.getStringArrayExtra("message");
		    //������������
		    updateChatView(new ChatEntity(
		    		Integer.parseInt(mes[2]),
		    		mes[1],
		    		mes[3],
		    		mes[4],
		    		true));
		}
	}
	public void updateChatView(ChatEntity chatEntity){
		chatEntityList.add(chatEntity);
		chatListView=(ListView) findViewById(R.id.lv_chat);
		chatListView.setAdapter(new ChatAdapter(this,chatEntityList));
	}

}
