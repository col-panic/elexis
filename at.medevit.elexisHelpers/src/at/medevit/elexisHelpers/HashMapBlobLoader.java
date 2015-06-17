package at.medevit.elexisHelpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.Set;
import java.util.zip.ZipInputStream;

public class HashMapBlobLoader {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		if(args.length<1) {
			System.out.println("Usage: java -jar HashMapBlobLoader.jar ExtInfoBlobFile");
			System.out.println("   where ExtInfoBlobFile - binary blob stored from ExtInfo DB");
			return;
		}
		
		System.out.println("File is: " + args[0]);
		FileInputStream fin = new FileInputStream(args[0]);
		ZipInputStream zis = new ZipInputStream(fin);
		zis.getNextEntry();
		ObjectInputStream ois = new ObjectInputStream(zis);
		Object readObject = ois.readObject();
		Hashtable ht = (Hashtable) readObject;
		Set keySet = ht.keySet();
		for (Object object : keySet) {
			System.out.println("[" + object + "] " + ht.get(object));
		}
	}
}
