package framework.telegram.ui.cameraview.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.exifinterface.media.ExifInterface;

import framework.telegram.ui.cameraview.Mapper1;
import framework.telegram.ui.cameraview.options.Facing;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Static utilities for dealing with camera I/O, orientations, etc.
 */
public class CameraUtils {


    /**
     * Determines whether the device has valid camera sensors, so the library
     * can be used.
     *
     * @param context a valid Context
     * @return whether device has cameras
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean hasCameras(@NonNull Context context) {
        PackageManager manager = context.getPackageManager();
        // There's also FEATURE_CAMERA_EXTERNAL , should we support it?
        return manager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || manager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }


    /**
     * Determines whether the device has a valid camera sensor with the given
     * Facing value, so that a session can be started.
     *
     * @param context a valid context
     * @param facing  either {@link Facing#BACK} or {@link Facing#FRONT}
     * @return true if such sensor exists
     */
    public static boolean hasCameraFacing(@NonNull Context context, @NonNull Facing facing) {
        int internal = new Mapper1().map(facing);
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0, count = Camera.getNumberOfCameras(); i < count; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == internal) return true;
        }
        return false;
    }


    /**
     * Simply writes the given data to the given file. It is done synchronously. If you are
     * running on the UI thread, please use {@link #writeToJpgFile(byte[], File, FileCallback)}
     * and pass a file callback.
     * <p>
     * If any error is encountered, this returns null.
     *
     * @param data the data to be written
     * @param file the file to write into
     * @return the source file, or null if error
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    @WorkerThread
    public static File writeToJpgFile(@NonNull final byte[] data, @NonNull File file) {
        if (file.exists() && !file.delete())
            return null;

        OutputStream stream = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            stream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bitmap.recycle();
            return file;
        } catch (IOException e) {
            return null;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Writes the given data to the given file in a background thread, returning on the
     * original thread (typically the UI thread) once writing is done.
     * If some error is encountered, the {@link FileCallback} will return null instead of the
     * original file.
     *
     * @param data     the data to be written
     * @param file     the file to write into
     * @param callback a callback
     */
    @SuppressWarnings("WeakerAccess")
    public static void writeToJpgFile(@NonNull final byte[] data, @NonNull final File file, @NonNull final FileCallback callback) {
        final Handler ui = new Handler();
        WorkerHandler.run(() -> {
            final File result = writeToJpgFile(data, file);
            ui.post(() -> callback.onFileReady(result));
        });
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     *
     * @param source a JPEG byte array
     * @return decoded bitmap or null if error is encountered
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    @WorkerThread
    public static Bitmap decodeBitmap(@NonNull final byte[] source) {
        return decodeBitmap(source, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * This is executed in a background thread, and returns the result to the original thread.
     *
     * @param source   a JPEG byte array
     * @param callback a callback to be notified
     */
    @SuppressWarnings("WeakerAccess")
    public static void decodeBitmap(@NonNull final byte[] source, @NonNull final BitmapCallback callback) {
        decodeBitmap(source, Integer.MAX_VALUE, Integer.MAX_VALUE, callback);
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * This is executed in a background thread, and returns the result to the original thread.
     * <p>
     * The image is also downscaled taking care of the maxWidth and maxHeight arguments.
     *
     * @param source    a JPEG byte array
     * @param maxWidth  the max allowed width
     * @param maxHeight the max allowed height
     * @param callback  a callback to be notified
     */
    @SuppressWarnings("WeakerAccess")
    public static void decodeBitmap(@NonNull final byte[] source, final int maxWidth, final int maxHeight, @NonNull final BitmapCallback callback) {
        decodeBitmap(source, maxWidth, maxHeight, new BitmapFactory.Options(), callback);
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * This is executed in a background thread, and returns the result to the original thread.
     * <p>
     * The image is also downscaled taking care of the maxWidth and maxHeight arguments.
     *
     * @param source    a JPEG byte array
     * @param maxWidth  the max allowed width
     * @param maxHeight the max allowed height
     * @param options   the options to be passed to decodeByteArray
     * @param callback  a callback to be notified
     */
    @SuppressWarnings("WeakerAccess")
    public static void decodeBitmap(@NonNull final byte[] source, final int maxWidth, final int maxHeight, @NonNull final BitmapFactory.Options options, @NonNull final BitmapCallback callback) {
        decodeBitmap(source, maxWidth, maxHeight, options, -1, callback);
    }

    @SuppressWarnings("WeakerAccess")
    public static void decodeBitmap(@NonNull final byte[] source, final int maxWidth, final int maxHeight, @NonNull final BitmapFactory.Options options, final int rotation, @NonNull final BitmapCallback callback) {
        final Handler ui = new Handler();
        WorkerHandler.run(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = decodeBitmap(source, maxWidth, maxHeight, options, rotation);
                ui.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onBitmapReady(bitmap);
                    }
                });
            }
        });
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * <p>
     * The image is also downscaled taking care of the maxWidth and maxHeight arguments.
     *
     * @param source    a JPEG byte array
     * @param maxWidth  the max allowed width
     * @param maxHeight the max allowed height
     */
    static Bitmap decodeBitmap(@NonNull byte[] source, int maxWidth, int maxHeight) {
        return decodeBitmap(source, maxWidth, maxHeight, new BitmapFactory.Options());
    }

    /**
     * Decodes an input byte array and outputs a Bitmap that is ready to be displayed.
     * The difference with {@link BitmapFactory#decodeByteArray(byte[], int, int)}
     * is that this cares about orientation, reading it from the EXIF header.
     * <p>
     * The image is also downscaled taking care of the maxWidth and maxHeight arguments.
     *
     * @param source    a JPEG byte array
     * @param maxWidth  the max allowed width
     * @param maxHeight the max allowed height
     * @param options   the options to be passed to decodeByteArray
     * @return decoded bitmap or null if error is encountered
     */
    @SuppressWarnings({"SuspiciousNameCombination", "WeakerAccess"})
    @Nullable
    @WorkerThread
    public static Bitmap decodeBitmap(@NonNull byte[] source, int maxWidth, int maxHeight, @NonNull BitmapFactory.Options options) {
        return decodeBitmap(source, maxWidth, maxHeight, options, -1);
    }

    // Null: got OOM
    // TODO ignores flipping. but it should be super rare.
    @Nullable
    static Bitmap decodeBitmap(@NonNull byte[] source, int maxWidth, int maxHeight, @NonNull BitmapFactory.Options options, int rotation) {
        if (maxWidth <= 0) maxWidth = Integer.MAX_VALUE;
        if (maxHeight <= 0) maxHeight = Integer.MAX_VALUE;
        int orientation;
        boolean flip;
        if (rotation == -1) {
            InputStream stream = null;
            try {
                // http://sylvana.net/jpegcrop/exif_orientation.html
                stream = new ByteArrayInputStream(source);
                ExifInterface exif = new ExifInterface(stream);
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                orientation = readExifOrientation(exifOrientation);
                flip = exifOrientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL ||
                        exifOrientation == ExifInterface.ORIENTATION_FLIP_VERTICAL ||
                        exifOrientation == ExifInterface.ORIENTATION_TRANSPOSE ||
                        exifOrientation == ExifInterface.ORIENTATION_TRANSVERSE;

            } catch (IOException e) {
                e.printStackTrace();
                orientation = 0;
                flip = false;
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception ignored) {
                    }
                }
            }
        } else {
            orientation = rotation;
            flip = false;
        }

        Bitmap bitmap;
        try {
            if (maxWidth < Integer.MAX_VALUE || maxHeight < Integer.MAX_VALUE) {
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(source, 0, source.length, options);

                int outHeight = options.outHeight;
                int outWidth = options.outWidth;
                if (orientation % 180 != 0) {
                    //noinspection SuspiciousNameCombination
                    outHeight = options.outWidth;
                    //noinspection SuspiciousNameCombination
                    outWidth = options.outHeight;
                }

                options.inSampleSize = computeSampleSize(outWidth, outHeight, maxWidth, maxHeight);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeByteArray(source, 0, source.length, options);
            } else {
                bitmap = BitmapFactory.decodeByteArray(source, 0, source.length);
            }

            if (orientation != 0 || flip) {
                Matrix matrix = new Matrix();
                matrix.setRotate(orientation);
                Bitmap temp = bitmap;
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                temp.recycle();
            }
        } catch (OutOfMemoryError e) {
            bitmap = null;
        }
        return bitmap;
    }

    public static int readExifOrientation(int exifOrientation) {
        int orientation;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_NORMAL:
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                orientation = 0;
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                orientation = 180;
                break;

            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_TRANSPOSE:
                orientation = 90;
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
            case ExifInterface.ORIENTATION_TRANSVERSE:
                orientation = 270;
                break;

            default:
                orientation = 0;
        }
        return orientation;
    }


    private static int computeSampleSize(int width, int height, int maxWidth, int maxHeight) {
        // https://developer.android.com/topic/performance/graphics/load-bitmap.html
        int inSampleSize = 1;
        if (height > maxHeight || width > maxWidth) {
            while ((height / inSampleSize) >= maxHeight
                    || (width / inSampleSize) >= maxWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


}
