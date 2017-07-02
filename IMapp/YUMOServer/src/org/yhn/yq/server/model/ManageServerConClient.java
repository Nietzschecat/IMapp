/**
 * ����ͻ������ӵ���
 */
package org.yhn.yq.server.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ManageServerConClient {
	public static HashMap hm=new HashMap<Integer,ServerConClientThread>();
	
	//���һ���ͻ���ͨ���߳�
	public static void addClientThread(int account, ServerConClientThread cc){
		hm.put(account,cc);
	}
	//�õ�һ���ͻ���ͨ���߳�
	public static ServerConClientThread getClientThread(int i){
		return (ServerConClientThread)hm.get(i);
	}
	//���ص�ǰ�����˵����
	public static List getAllOnLineUserid(){
		List list=new ArrayList();
		//ʹ�õ��������
		Iterator it=hm.keySet().iterator();
		while(it.hasNext()){
			list.add((int) it.next());
		}
		return list;
	}
	
	public static boolean isOnline(int a){
		List list=new ArrayList();
		//ʹ�õ��������
		Iterator it=hm.keySet().iterator();
		while(it.hasNext()){
			int account=(int) it.next();
			if(a==account){
				return true;
			}
		}
		return false;
	}
}
