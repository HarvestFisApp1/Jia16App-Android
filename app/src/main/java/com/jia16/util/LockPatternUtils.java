/*
 * 文 件 名:  LockPatternUtils.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  jiaohongyun
 * 修改时间:  2014年12月31日
 */
package com.jia16.util;

import android.content.Context;
import android.os.FileObserver;
import android.util.Log;


import com.jia16.view.LockPatternView;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author jiaohongyun
 * @version [版本号, 2014年12月31日]
 */
public class LockPatternUtils {
    public static final int MIN_LOCK_PATTERN_SIZE = 4;
    public static final int FAILED_ATTEMPTS_BEFORE_TIMEOUT = 5;
    public static final int MIN_PATTERN_REGISTER_FAIL = MIN_LOCK_PATTERN_SIZE;
    public static final long FAILED_ATTEMPT_TIMEOUT_MS = 30000L;
    private static final String TAG = "LockPatternUtils";
    private static final String LOCK_PATTERN_FILE = "gesture.key";
    private static final AtomicBoolean sHaveNonZeroPatternFile = new AtomicBoolean(false);
    private static File sLockPatternFilename;
    private static FileObserver sPasswordObserver;

    private String checkPassword;

    public LockPatternUtils(Context context) {
        if (sLockPatternFilename == null) {
            String dataSystemDirectory = context.getFilesDir().getAbsolutePath();
            sLockPatternFilename = new File(dataSystemDirectory, LOCK_PATTERN_FILE);
            sHaveNonZeroPatternFile.set(sLockPatternFilename.length() > 0);
            int fileObserverMask = FileObserver.CLOSE_WRITE | FileObserver.DELETE | FileObserver.MOVED_TO | FileObserver.CREATE;
            sPasswordObserver = new LockPatternFileObserver(dataSystemDirectory, fileObserverMask);
            sPasswordObserver.startWatching();
        }
    }

    public static List<LockPatternView.Cell> stringToPattern(String string) {
        List<LockPatternView.Cell> result = new ArrayList<LockPatternView.Cell>();

        for (int i = 0; i < string.length(); i++) {
            //            char c = string.charAt(i);
            String a = string.substring(i, i + 1);
            int b = Integer.valueOf(a);
            int c = b / 3;
            int d = b % 3;
            result.add(LockPatternView.Cell.of(c, d));
        }
        return result;
    }

    public static String patternToString(List<LockPatternView.Cell> pattern) {
        if (pattern == null) {
            return "";
        }
        final int patternSize = pattern.size();
        String result = "";
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            LockPatternView.Cell cell = pattern.get(i);
            res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
            result += cell.getRow() * 3 + cell.getColumn();
        }
        return result;
        //return new String(res);
    }

    @SuppressWarnings("unused")
    private static byte[] patternToHash(List<LockPatternView.Cell> pattern) {
        if (pattern == null) {
            return null;
        }

        final int patternSize = pattern.size();
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            LockPatternView.Cell cell = pattern.get(i);
            res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(res);
            return hash;
        } catch (NoSuchAlgorithmException nsa) {
            return res;
        }
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    //    public static List<LockPatternView.Cell> stringToPattern(String string)
    //    {
    //        List<LockPatternView.Cell> result = new ArrayList<LockPatternView.Cell>();
    //
    //        final byte[] bytes = string.getBytes();
    //        for (int i = 0; i < bytes.length; i++)
    //        {
    //            byte b = bytes[i];
    //            result.add(LockPatternView.Cell.of(b / 3, b % 3));
    //        }
    //        return result;
    //    }

    public boolean savedPatternExists() {
        return sHaveNonZeroPatternFile.get();
    }

    public void clearLock() {
        //        App.getInstance().getDemengApp().propJs("DMJS.localData.setByUser('handPassword',undefined)");
        //        saveLockPattern(null);
    }

    public void saveLockPattern(List<LockPatternView.Cell> pattern) {
        //        // Compute the hash
        //        String passwrod = this.patternToString(pattern);
        //        App.getInstance().getDemengApp().propJs("DMJS.localData.setByUser('handPassword','" + passwrod + "')");
        //        App.getInstance().setHansPassword(passwrod);
        //        final byte[] hash = LockPatternUtils.patternToHash(pattern);
        //        try
        //        {
        //            // Write the hash to file
        //            RandomAccessFile raf = new RandomAccessFile(sLockPatternFilename, "rwd");
        //            // Truncate the file if pattern is null, to clear the lock
        //            if (pattern == null)
        //            {
        //                raf.setLength(0);
        //            }
        //            else
        //            {
        //                raf.write(hash, 0, hash.length);
        //            }
        //            raf.close();
        //        }
        //        catch (FileNotFoundException fnfe)
        //        {
        //            // Cant do much, unless we want to fail over to using the settings
        //            // provider
        //            Log.e(TAG, "Unable to save lock pattern to " + sLockPatternFilename);
        //        }
        //        catch (IOException ioe)
        //        {
        //            // Cant do much
        //            Log.e(TAG, "Unable to save lock pattern to " + sLockPatternFilename);
        //        }
    }

    public boolean checkPattern(List<LockPatternView.Cell> pattern) {
        @SuppressWarnings("static-access")
        String n_password = this.patternToString(pattern);
        return this.checkPassword.equals(n_password);
        //      try {
        //          // Read all the bytes from the file
        //
        //          RandomAccessFile raf = new RandomAccessFile(sLockPatternFilename,
        //                  "r");
        //          final byte[] stored = new byte[(int) raf.length()];
        //          int got = raf.read(stored, 0, stored.length);
        //          raf.close();
        //          if (got <= 0) {
        //              return true;
        //          }
        //          // Compare the hash from the file with the entered pattern's hash
        //          return Arrays.equals(stored,
        //                  LockPatternUtils.patternToHash(pattern));
        //      } catch (FileNotFoundException fnfe) {
        //          return true;
        //      } catch (IOException ioe) {
        //          return true;
        //      }
    }

    private static class LockPatternFileObserver extends FileObserver {
        public LockPatternFileObserver(String path, int mask) {
            super(path, mask);
        }

        @Override
        public void onEvent(int event, String path) {
            Log.d(TAG, "file path" + path);
            if (LOCK_PATTERN_FILE.equals(path)) {
                Log.d(TAG, "lock pattern file changed");
                sHaveNonZeroPatternFile.set(sLockPatternFilename.length() > 0);
            }
        }
    }
}
