package Client.Func;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileTransfer {
    public File oldFile;//传进的文件
    public File newFile;//需写入的路径
    public FileTransfer(File oldFile, File newFile){
        this.oldFile=oldFile;
        this.newFile=newFile;
        String oldpath = oldFile.getAbsolutePath();
        String newpath = newFile.getAbsolutePath();
        List<File> list=new ArrayList<File>();
        if(oldFile.isDirectory()) {	//判断输入的是否为路径
            getAllFile(oldFile,list);	//获取oldFile里的所有文件
            backUp(list,oldpath,newpath);	//将oldFile里所有文件拷贝到newFile里
        }
        if(oldFile.isFile()) {		//判断输入的是否为文件
            String newFileName=oldpath.substring(oldpath.lastIndexOf("\\")+1);	//截取文件名
            File newf=new File(newpath+File.separator+newFileName);		//在目标目录下创建与该文件名相同的文件
            copy(oldFile,newf);		//文件拷贝
        }
    }

    //备份，循环list，判断这个File是否为一个文件
    public static void backUp(List<File> list, String oldpath,String newpath) {
        if(list==null||list.size()<=0) {	//判断传进来的链表是否为有效链表
            return;
        }
        for(File f:list) {
            String fpath=f.getAbsolutePath();//取出源路径
            String newPath=fpath.replace(oldpath, newpath);//将源路径的部分替换成备份路径
            File newFile=new File(newPath);	//创建文件对象
            if(newFile.getParentFile().exists()==false) {	//生成目录
                newFile.getParentFile().mkdirs();
            }
            if(f.isFile()) {
                copy(f,newFile);
            }
        }
    }

    //递归获取oldFile下所有文件信息
    public void getAllFile(File oldFile, List<File> list) {
        if(oldFile.isDirectory()) {
            File[] fs=oldFile.listFiles();
            if(fs!=null&&fs.length>0) {//不备份空的文件夹
                for(File file1:fs) {
                    getAllFile(file1,list);	//递归获取所有文件信息
                }
            }
        }
        list.add(oldFile);	//将文件加入列表，方便复制粘贴
    }

    //复制
    public static void copy(File inFile, File outFile){
        FileInputStream fis=null;
        FileOutputStream fos=null;
        try {
            fis=new FileInputStream(inFile);//读取
            fos=new FileOutputStream(outFile);//写入
            byte[] bs=new byte[1024];
            int length=-1;
            while((length=fis.read(bs))!=-1) {
                fos.write(bs,0,length);
            }
            fos.flush();	//刷新
        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            if(fos!=null) {
                try {
                    fos.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
            if(fis!=null) {
                try {
                    fis.close();
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
