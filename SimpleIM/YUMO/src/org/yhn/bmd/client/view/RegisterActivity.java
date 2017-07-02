package org.yhn.bmd.client.view;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import org.bmd.yq.R;
import org.bmd.yq.client.model.ClientConServerThread;
import org.bmd.yq.client.model.ManageClientConServer;
import org.bmd.yq.client.model.YQClient;
import org.yhn.bmd.common.MyTime;
import org.yhn.bmd.common.User;
import org.yhn.bmd.common.YQMessage;
import org.yhn.bmd.common.YQMessageType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	String sex="男";
	Socket s;
    boolean b=false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_register);
		findViewById(R.id.rigister_btn_register).setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				EditText accountEt=(EditText) findViewById(R.id.register_account);
				EditText passwordEt=(EditText) findViewById(R.id.register_password);
				EditText nickEt=(EditText) findViewById(R.id.register_nick);
				RadioGroup group = (RadioGroup)findViewById(R.id.register_radiogroup);
				group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup arg0,int id) {
						if(id==R.id.register_radio_nv){
							sex="女";
						}
					}
				});
				if(accountEt.getText().equals("") || passwordEt.getText().equals("")){
					Toast.makeText(RegisterActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
				}else {
					final User user=new User();
					user.setAccount(Integer.parseInt(accountEt.getText().toString()));
					user.setPassword(passwordEt.getText().toString());
					user.setNick(nickEt.getText().toString());
					user.setTrends("该用户暂时没有动态");
					user.setSex(sex);
					user.setAvatar(4);
					user.setLev(0);
					user.setAge(0);
					user.setTime(MyTime.geTimeNoS());
					user.setOperation("register");
					new Thread()
	                {
	                    @Override
	                    public void run() {
	                        super.run();

	                        try {
	    						s=new Socket();
	    					    s.connect(new InetSocketAddress("10.252.236.53",4396),2000);
	    						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
		                		oos.writeObject(user);
		                		ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
		                		YQMessage ms=(YQMessage)ois.readObject();
	    						if(ms.getType().equals(YQMessageType.SUCCESS)){
	    						b=true;
	    							//Toast.makeText(RegisterActivity.this, "恭喜你，注册成功 ！", Toast.LENGTH_SHORT).show();
	    						}
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    					} catch (ClassNotFoundException e) {
	    						e.printStackTrace();
	    					}
	                    }
	                  }.start();
	                  
				}
				if(b){
					Toast.makeText(RegisterActivity.this, "恭喜你，注册成功 ！请登录吧", Toast.LENGTH_SHORT).show();
					startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
					
				}
			}
		});
		

		}	
}
