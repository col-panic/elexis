package at.medevit.elexisHelpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

import ch.elexis.data.Kontakt.statL;
import ch.elexis.util.MFUList;
import ch.rgw.compress.CompEx;
import ch.rgw.tools.StringTool;

public class HashMapBlobLoader {
	public static void main(String[] args) throws IOException, ClassNotFoundException{
		if (args.length < 1) {
			System.out.println("Usage: java -jar HashMapBlobLoader.jar ExtInfoBlobFile");
			System.out.println("   where ExtInfoBlobFile - binary blob stored from ExtInfo DB");
			return;
		}
		
		System.out.println("File is: " + args[0]);
		FileInputStream fin = new FileInputStream(args[0]);
		ZipInputStream zis = new ZipInputStream(fin);
		if (zis.getNextEntry() != null) {
			ObjectInputStream ois = new ObjectInputStream(zis);
			Object readObject = ois.readObject();
			Hashtable ht = (Hashtable) readObject;
			Set keySet = ht.keySet();
			for (Object object : keySet) {
				System.out.println("[" + object + "]");
				String val = ts(ht.get(object));
				if (val != null) {
					System.out.println("\t " + val);
				}
			}
		} else {
			zis.close();
			FileInputStream fileInputStream = new FileInputStream(args[0]);
			byte[] val = CompEx.expand(fileInputStream);
			System.out.println(StringTool.createString(val));
			fileInputStream.close();
		}
	}
	
	private static String ts(Object object){
		if (object instanceof List) {
			List l = (List) object;
			for (int i = 0; i < l.size(); i++) {
				Object o = l.get(i);
				System.out.println("\t " + i + "/" + l.size() + ": " + ts(o));
			}
			return null;
		} else if (object instanceof statL) {
			statL s = (statL) object;
			try {
				Field declaredField = statL.class.getDeclaredField("v");
				declaredField.setAccessible(true);
				return statL.class.getName() + " " + declaredField.get(s);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
					| IllegalAccessException e) {
				return statL.class.getName() + " " + e.getMessage();
			}
		} else if (object instanceof MFUList) {
			MFUList ml = (MFUList) object;
			System.out.println("\t -> " + MFUList.class.getName());
			return ts(ml.getAll());
		}
		
		try {
			// try to unpack as list of strings (e.g. Rechnungen)
			List<String> list = StringTool.unpack((byte[]) object);
			for (int i = 0; i < list.size(); i++) {
				String s = list.get(i);
				System.out.println("\t " + i + "/" + list.size() + ": " + s);
			}
			return null;
		} catch (ClassCastException ce) {}
		
		return object.toString();
	}
}
