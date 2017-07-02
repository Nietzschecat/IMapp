package org.yhn.yq.client.view;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.yhn.yq.R;
import org.yhn.yq.client.model.ClientConServerThread;
import org.yhn.yq.client.model.ManageClientConServer;
import org.yhn.yq.client.model.YQClient;
import org.yhn.yq.common.User;
import org.yhn.yq.common.YQMessage;
import org.yhn.yq.common.YQMessageType;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public static String userInfo;
	EditText accountEt,passwordEt;
	Dialog dialog;
	Socket s;
	boolean b = false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_login);
	    accountEt=(EditText) findViewById(R.id.et_account);
	    passwordEt=(EditText) findViewById(R.id.et_password);
	    Button btnLogin=(Button) findViewById(R.id.btn_login);
	    
	    btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if(accountEt.getText().equals("") || passwordEt.getText().equals("")){
					Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					final User user=new User();
					user.setAccount(Integer.parseInt(accountEt.getText().toString()));
					user.setPassword(passwordEt.getText().toString());
					user.setOperation("login");
					//dialog = ProgressDialog.show(LoginActivity.this, "YQ",  "正在登陆中 …", true, true);
					//handler.post(new Runnable(){
					//	public void run() {
				    Toast.makeText(LoginActivity.this, "已经进入线程", Toast.LENGTH_SHORT).show();
					//boolean b=login(Integer.parseInt(accountEt.getText().toString()), passwordEt.getText().toString());
///////////////////////////////////////////////////////////////////}
							new Thread()
			                {
			                    @Override
			                    public void run() {
			                        super.run();
			                        try {
			                            //创建Socket
			                            Socket s=new Socket("169.254.179.38",4396);
			                            ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			                			oos.writeObject(user);
			                			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			                			YQMessage ms=(YQMessage)ois.readObject();
			                			if(ms.getType().equals(YQMessageType.SUCCESS)){
			                				//个人信息
			                			    MainActivity.myInfo=ms.getContent();
			                				//创建一个该账号和服务器保持连接的线程
			                			    //-------!!!!!!!!!!!!!!!
			                			    ClientConServerThread ccst=new ClientConServerThread(getBaseContext(),s);
			                				//启动该通信线程
			                				ccst.start();
			                				//加入到管理类中
			                				ManageClientConServer.addClientConServerThread(user.getAccount(), ccst);
			                				try {
												//发送一个要求返回在线好友的请求的Message
												ObjectOutputStream oos2 = new ObjectOutputStream	(
														ManageClientConServer.getClientConServerThread(user.getAccount()).getS().getOutputStream());
												YQMessage m=new YQMessage();
												m.setType(YQMessageType.GET_ONLINE_FRIENDS);
												m.setSender(user.getAccount());
												oos2.writeObject(m);
											} catch (IOException e) {
												e.printStackTrace();
											}
			                				//
			                				b=true;
			                			}else{
			                				b=false;
			                			}
										if(b){
											startActivity(new Intent(LoginActivity.this, MainActivity.class));
										}
			                            //socket.close();
			                        } catch (IOException e) {
			                            e.printStackTrace();
			                        } catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			                    }
			                }.start();
					        if(!b)
					        {
					        	//Toast.makeText(LoginActivity.this, "登陆失败！不告诉你为什么，", Toast.LENGTH_SHORT).show();
					        }
				}
			}
	    });
	    
	    findViewById(R.id.btn_register).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
			}
	    });
	    ManageActivity.addActiviy("LoginActivity", this);
	}
}


/**
	boolean login(int a, String p){
		User user=new User();
		user.setAccount(a);
		user.setPassword(p);
		user.setOperation("login");
		boolean b=new YQClient(this).sendLoginInfo(user);
		//登陆成功
		if(b){
			try {
				//发送一个要求返回在线好友的请求的Message
				ObjectOutputStream oos = new ObjectOutputStream	(
						ManageClientConServer.getClientConServerThread(user.getAccount()).getS().getOutputStream());
				YQMessage m=new YQMessage();
				m.setType(YQMessageType.GET_ONLINE_FRIENDS);
				m.setSender(user.getAccount());
				oos.writeObject(m);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.finish();
			return true;
		}else {
			return false;
		}
	}
	private Handler handler=new Handler(){  
        public void handleMessage(Message msg){  
            switch(msg.what){  
            case 1:
                dialog.dismiss();  
                break;  
            }  
        }  
    }; 
**/

