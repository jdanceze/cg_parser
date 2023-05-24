package android.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.FileNotFoundException;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore.class */
public final class MediaStore {
    public static final String AUTHORITY = "media";
    @Deprecated
    public static final String INTENT_ACTION_MUSIC_PLAYER = "android.intent.action.MUSIC_PLAYER";
    public static final String INTENT_ACTION_MEDIA_SEARCH = "android.intent.action.MEDIA_SEARCH";
    public static final String INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH = "android.media.action.MEDIA_PLAY_FROM_SEARCH";
    public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
    public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
    public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
    public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";
    public static final String EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation";
    public static final String EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen";
    public static final String EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons";
    public static final String EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion";
    public static final String INTENT_ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA";
    public static final String INTENT_ACTION_VIDEO_CAMERA = "android.media.action.VIDEO_CAMERA";
    public static final String ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE";
    public static final String ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
    public static final String EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality";
    public static final String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";
    public static final String EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit";
    public static final String EXTRA_OUTPUT = "output";
    public static final String UNKNOWN_STRING = "<unknown>";
    public static final String MEDIA_SCANNER_VOLUME = "volume";
    public static final String MEDIA_IGNORE_FILENAME = ".nomedia";

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$MediaColumns.class */
    public interface MediaColumns extends BaseColumns {
        public static final String DATA = "_data";
        public static final String SIZE = "_size";
        public static final String DISPLAY_NAME = "_display_name";
        public static final String TITLE = "title";
        public static final String DATE_ADDED = "date_added";
        public static final String DATE_MODIFIED = "date_modified";
        public static final String MIME_TYPE = "mime_type";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Files.class */
    public static final class Files {

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Files$FileColumns.class */
        public interface FileColumns extends MediaColumns {
            public static final String PARENT = "parent";
            public static final String MIME_TYPE = "mime_type";
            public static final String TITLE = "title";
            public static final String MEDIA_TYPE = "media_type";
            public static final int MEDIA_TYPE_NONE = 0;
            public static final int MEDIA_TYPE_IMAGE = 1;
            public static final int MEDIA_TYPE_AUDIO = 2;
            public static final int MEDIA_TYPE_VIDEO = 3;
            public static final int MEDIA_TYPE_PLAYLIST = 4;
        }

        public Files() {
            throw new RuntimeException("Stub!");
        }

        public static Uri getContentUri(String volumeName) {
            throw new RuntimeException("Stub!");
        }

        public static final Uri getContentUri(String volumeName, long rowId) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Images.class */
    public static final class Images {

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Images$ImageColumns.class */
        public interface ImageColumns extends MediaColumns {
            public static final String DESCRIPTION = "description";
            public static final String PICASA_ID = "picasa_id";
            public static final String IS_PRIVATE = "isprivate";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DATE_TAKEN = "datetaken";
            public static final String ORIENTATION = "orientation";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String BUCKET_ID = "bucket_id";
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Images$Media.class */
        public static final class Media implements ImageColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image";
            public static final String DEFAULT_SORT_ORDER = "bucket_display_name";

            public Media() {
                throw new RuntimeException("Stub!");
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
                throw new RuntimeException("Stub!");
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection, String where, String orderBy) {
                throw new RuntimeException("Stub!");
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                throw new RuntimeException("Stub!");
            }

            public static final Bitmap getBitmap(ContentResolver cr, Uri url) throws FileNotFoundException, IOException {
                throw new RuntimeException("Stub!");
            }

            public static final String insertImage(ContentResolver cr, String imagePath, String name, String description) throws FileNotFoundException {
                throw new RuntimeException("Stub!");
            }

            public static final String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Images$Thumbnails.class */
        public static class Thumbnails implements BaseColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String DEFAULT_SORT_ORDER = "image_id ASC";
            public static final String DATA = "_data";
            public static final String IMAGE_ID = "image_id";
            public static final String KIND = "kind";
            public static final int MINI_KIND = 1;
            public static final int FULL_SCREEN_KIND = 2;
            public static final int MICRO_KIND = 3;
            public static final String THUMB_DATA = "thumb_data";
            public static final String WIDTH = "width";
            public static final String HEIGHT = "height";

            public Thumbnails() {
                throw new RuntimeException("Stub!");
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
                throw new RuntimeException("Stub!");
            }

            public static final Cursor queryMiniThumbnails(ContentResolver cr, Uri uri, int kind, String[] projection) {
                throw new RuntimeException("Stub!");
            }

            public static final Cursor queryMiniThumbnail(ContentResolver cr, long origId, int kind, String[] projection) {
                throw new RuntimeException("Stub!");
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId) {
                throw new RuntimeException("Stub!");
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
                throw new RuntimeException("Stub!");
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId, long groupId) {
                throw new RuntimeException("Stub!");
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, BitmapFactory.Options options) {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        public Images() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio.class */
    public static final class Audio {

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$AlbumColumns.class */
        public interface AlbumColumns {
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM = "album";
            public static final String ARTIST = "artist";
            public static final String NUMBER_OF_SONGS = "numsongs";
            public static final String NUMBER_OF_SONGS_FOR_ARTIST = "numsongs_by_artist";
            public static final String FIRST_YEAR = "minyear";
            public static final String LAST_YEAR = "maxyear";
            public static final String ALBUM_KEY = "album_key";
            public static final String ALBUM_ART = "album_art";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$ArtistColumns.class */
        public interface ArtistColumns {
            public static final String ARTIST = "artist";
            public static final String ARTIST_KEY = "artist_key";
            public static final String NUMBER_OF_ALBUMS = "number_of_albums";
            public static final String NUMBER_OF_TRACKS = "number_of_tracks";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$AudioColumns.class */
        public interface AudioColumns extends MediaColumns {
            public static final String TITLE_KEY = "title_key";
            public static final String DURATION = "duration";
            public static final String BOOKMARK = "bookmark";
            public static final String ARTIST_ID = "artist_id";
            public static final String ARTIST = "artist";
            public static final String ARTIST_KEY = "artist_key";
            public static final String COMPOSER = "composer";
            public static final String ALBUM_ID = "album_id";
            public static final String ALBUM = "album";
            public static final String ALBUM_KEY = "album_key";
            public static final String TRACK = "track";
            public static final String YEAR = "year";
            public static final String IS_MUSIC = "is_music";
            public static final String IS_PODCAST = "is_podcast";
            public static final String IS_RINGTONE = "is_ringtone";
            public static final String IS_ALARM = "is_alarm";
            public static final String IS_NOTIFICATION = "is_notification";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$GenresColumns.class */
        public interface GenresColumns {
            public static final String NAME = "name";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$PlaylistsColumns.class */
        public interface PlaylistsColumns {
            public static final String NAME = "name";
            public static final String DATA = "_data";
            public static final String DATE_ADDED = "date_added";
            public static final String DATE_MODIFIED = "date_modified";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Media.class */
        public static final class Media implements AudioColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";
            public static final String DEFAULT_SORT_ORDER = "title_key";
            public static final String RECORD_SOUND_ACTION = "android.provider.MediaStore.RECORD_SOUND";
            public static final String EXTRA_MAX_BYTES = "android.provider.MediaStore.extra.MAX_BYTES";

            public Media() {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUriForPath(String path) {
                throw new RuntimeException("Stub!");
            }
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Genres.class */
        public static final class Genres implements BaseColumns, GenresColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/genre";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";
            public static final String DEFAULT_SORT_ORDER = "name";

            /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Genres$Members.class */
            public static final class Members implements AudioColumns {
                public static final String CONTENT_DIRECTORY = "members";
                public static final String DEFAULT_SORT_ORDER = "title_key";
                public static final String AUDIO_ID = "audio_id";
                public static final String GENRE_ID = "genre_id";

                public Members() {
                    throw new RuntimeException("Stub!");
                }

                public static final Uri getContentUri(String volumeName, long genreId) {
                    throw new RuntimeException("Stub!");
                }
            }

            public Genres() {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUriForAudioId(String volumeName, int audioId) {
                throw new RuntimeException("Stub!");
            }
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Playlists.class */
        public static final class Playlists implements BaseColumns, PlaylistsColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/playlist";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";
            public static final String DEFAULT_SORT_ORDER = "name";

            /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Playlists$Members.class */
            public static final class Members implements AudioColumns {
                public static final String _ID = "_id";
                public static final String CONTENT_DIRECTORY = "members";
                public static final String AUDIO_ID = "audio_id";
                public static final String PLAYLIST_ID = "playlist_id";
                public static final String PLAY_ORDER = "play_order";
                public static final String DEFAULT_SORT_ORDER = "play_order";

                public Members() {
                    throw new RuntimeException("Stub!");
                }

                public static final Uri getContentUri(String volumeName, long playlistId) {
                    throw new RuntimeException("Stub!");
                }

                public static final boolean moveItem(ContentResolver res, long playlistId, int from, int to) {
                    throw new RuntimeException("Stub!");
                }
            }

            public Playlists() {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Artists.class */
        public static final class Artists implements BaseColumns, ArtistColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/artists";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";
            public static final String DEFAULT_SORT_ORDER = "artist_key";

            /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Artists$Albums.class */
            public static final class Albums implements AlbumColumns {
                public Albums() {
                    throw new RuntimeException("Stub!");
                }

                public static final Uri getContentUri(String volumeName, long artistId) {
                    throw new RuntimeException("Stub!");
                }
            }

            public Artists() {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Audio$Albums.class */
        public static final class Albums implements BaseColumns, AlbumColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/albums";
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";
            public static final String DEFAULT_SORT_ORDER = "album_key";

            public Albums() {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        public Audio() {
            throw new RuntimeException("Stub!");
        }

        public static String keyFor(String name) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Video.class */
    public static final class Video {
        public static final String DEFAULT_SORT_ORDER = "_display_name";

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Video$VideoColumns.class */
        public interface VideoColumns extends MediaColumns {
            public static final String DURATION = "duration";
            public static final String ARTIST = "artist";
            public static final String ALBUM = "album";
            public static final String RESOLUTION = "resolution";
            public static final String DESCRIPTION = "description";
            public static final String IS_PRIVATE = "isprivate";
            public static final String TAGS = "tags";
            public static final String CATEGORY = "category";
            public static final String LANGUAGE = "language";
            public static final String LATITUDE = "latitude";
            public static final String LONGITUDE = "longitude";
            public static final String DATE_TAKEN = "datetaken";
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";
            public static final String BUCKET_ID = "bucket_id";
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
            public static final String BOOKMARK = "bookmark";
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Video$Media.class */
        public static final class Media implements VideoColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";
            public static final String DEFAULT_SORT_ORDER = "title";

            public Media() {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/provider/MediaStore$Video$Thumbnails.class */
        public static class Thumbnails implements BaseColumns {
            public static final Uri INTERNAL_CONTENT_URI = null;
            public static final Uri EXTERNAL_CONTENT_URI = null;
            public static final String DEFAULT_SORT_ORDER = "video_id ASC";
            public static final String DATA = "_data";
            public static final String VIDEO_ID = "video_id";
            public static final String KIND = "kind";
            public static final int MINI_KIND = 1;
            public static final int FULL_SCREEN_KIND = 2;
            public static final int MICRO_KIND = 3;
            public static final String WIDTH = "width";
            public static final String HEIGHT = "height";

            public Thumbnails() {
                throw new RuntimeException("Stub!");
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId) {
                throw new RuntimeException("Stub!");
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
                throw new RuntimeException("Stub!");
            }

            public static Bitmap getThumbnail(ContentResolver cr, long origId, long groupId, int kind, BitmapFactory.Options options) {
                throw new RuntimeException("Stub!");
            }

            public static void cancelThumbnailRequest(ContentResolver cr, long origId, long groupId) {
                throw new RuntimeException("Stub!");
            }

            public static Uri getContentUri(String volumeName) {
                throw new RuntimeException("Stub!");
            }
        }

        public Video() {
            throw new RuntimeException("Stub!");
        }

        public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
            throw new RuntimeException("Stub!");
        }
    }

    public MediaStore() {
        throw new RuntimeException("Stub!");
    }

    public static Uri getMediaScannerUri() {
        throw new RuntimeException("Stub!");
    }

    public static String getVersion(Context context) {
        throw new RuntimeException("Stub!");
    }
}
