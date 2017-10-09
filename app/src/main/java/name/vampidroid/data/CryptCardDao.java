package name.vampidroid.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by francisco on 31/08/17.
 */

@Dao
public abstract class CryptCardDao {

    @Query("select * from CryptCard where uid = :id")
    public abstract Flowable<CryptCard> getById(long id);

    public static List<CryptCard> convertCursorToCryptCardList(Cursor cursor) {
        final Cursor _cursor = cursor;
        try {
            final int _cursorIndexOfClan = _cursor.getColumnIndexOrThrow("clan");
            final int _cursorIndexOfDisciplines = _cursor.getColumnIndexOrThrow("disciplines");
            final int _cursorIndexOfCapacity = _cursor.getColumnIndexOrThrow("capacity");
            final int _cursorIndexOfGroup = _cursor.getColumnIndexOrThrow("group");
            final int _cursorIndexOfAdvanced = _cursor.getColumnIndexOrThrow("advanced");
            final int _cursorIndexOfUid = _cursor.getColumnIndexOrThrow("uid");
            final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
            final List<CryptCard> _result = new ArrayList<CryptCard>(_cursor.getCount());
            while(_cursor.moveToNext()) {
                final CryptCard _item;
                final String _tmpClan;
                _tmpClan = _cursor.getString(_cursorIndexOfClan);
                final String _tmpDisciplines;
                _tmpDisciplines = _cursor.getString(_cursorIndexOfDisciplines);
                final String _tmpCapacity;
                _tmpCapacity = _cursor.getString(_cursorIndexOfCapacity);
                final String _tmpGroup;
                _tmpGroup = _cursor.getString(_cursorIndexOfGroup);
                final String _tmpAdvanced;
                _tmpAdvanced = _cursor.getString(_cursorIndexOfAdvanced);
                final String _tmpName;
                _tmpName = _cursor.getString(_cursorIndexOfName);

                _item = new CryptCard(_tmpName,"",_tmpClan,_tmpDisciplines,"",_tmpCapacity,"","",_tmpGroup,_tmpAdvanced);
                final Long _tmpUid;
                if (_cursor.isNull(_cursorIndexOfUid)) {
                    _tmpUid = null;
                } else {
                    _tmpUid = _cursor.getLong(_cursorIndexOfUid);
                }
                _item.setUid(_tmpUid);
                _result.add(_item);
            }
            return _result;
        } finally {
            _cursor.close();
        }
    }
}
