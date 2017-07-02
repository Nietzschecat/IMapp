package org.bmd.yq.server.model;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.bmd.yq.common.User;
import org.bmd.yq.common.YQMessage;
import org.bmd.yq.common.YQMessageType;
import org.bmd.yq.server.dao.UserDao;

public class YQServer {
	public YQServer(){
		ServerSocket ss = null;
		try {
			ss=new ServerSocket(4396);
			System.out.println("������������ in "+new Date());
			while(true){
				Socket s=ss.accept();
				//���ܿͻ��˷�������Ϣ
				System.out.println("�Ѿ����û�����������");
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				User u=(User) ois.readObject();
				YQMessage m=new YQMessage();
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
		        if(u.getOperation().equals("login")){ //��¼
		        	System.out.println("�û����еĲ����ǵ�¼");
		        	int account=u.getAccount();
		        	UserDao udao=new UserDao();
		        	boolean b=udao.login(account, u.getPassword());//�������ݿ���֤�û�
					if(b){
						System.out.println("["+account+"]�����ˣ�");
						//�������ݿ��û�״̬
						udao.changeState(account, 1);
						//�õ�������Ϣ
						String user=udao.getUser(account);
						//����һ���ɹ���½����Ϣ����������������Ϣ
						m.setType(YQMessageType.SUCCESS);
						m.setContent(user);
						oos.writeObject(m);
						ServerConClientThread cct=new ServerConClientThread(s);//����һ���̣߳��ø��߳���ÿͻ��˱�������
						ManageServerConClient.addClientThread(u.getAccount(),cct);
						cct.start();//������ÿͻ���ͨ�ŵ��߳�
					}else{
						m.setType(YQMessageType.FAIL);
						oos.writeObject(m);
					}
		        }else if(u.getOperation().equals("register")){
					System.out.println("�û����еĲ�����ע��,ע����ϢΪ��");
		        	int account=u.getAccount();
					System.out.println("�˻���"+account);
					String password=u.getPassword();
					System.out.println("���룺"+password);
		        	UserDao udao=new UserDao();
		        	if(udao.register(u)){
		        		//����һ��ע��ɹ�����Ϣ��
						m.setType(YQMessageType.SUCCESS);
						oos.writeObject(m);
		        	}else{
						m.setType(YQMessageType.FAIL);
						oos.writeObject(m);
					}
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}
