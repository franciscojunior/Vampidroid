package name.vampidroid.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by francisco on 11/09/17.
 */

@Dao
public abstract class LibraryCardDao {

    @Query("select * from LibraryCard where uid = :id")
    public abstract Flowable<LibraryCard> getById(long id);

    public static List<LibraryCard> convertCursorToLibraryCardList(Cursor cursor) {
        final Cursor _cursor = cursor;
        try {
            final int _cursorIndexOfPoolCost = _cursor.getColumnIndexOrThrow("poolCost");
            final int _cursorIndexOfBloodCost = _cursor.getColumnIndexOrThrow("bloodCost");
            final int _cursorIndexOfConvictionCost = _cursor.getColumnIndexOrThrow("convictionCost");
            final int _cursorIndexOfUid = _cursor.getColumnIndexOrThrow("uid");
            final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
            final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
            final int _cursorIndexOfDisciplines = _cursor.getColumnIndexOrThrow("disciplines");
            final List<LibraryCard> _result = new ArrayList<LibraryCard>(_cursor.getCount());
            while(_cursor.moveToNext()) {
                final LibraryCard _item;
                final String _tmpPoolCost;
                _tmpPoolCost = _cursor.getString(_cursorIndexOfPoolCost);
                final String _tmpBloodCost;
                _tmpBloodCost = _cursor.getString(_cursorIndexOfBloodCost);
                final String _tmpConvictionCost;
                _tmpConvictionCost = _cursor.getString(_cursorIndexOfConvictionCost);
                final String _tmpName;
                _tmpName = _cursor.getString(_cursorIndexOfName);
                final String _tmpType;
                _tmpType = _cursor.getString(_cursorIndexOfType);
                final String _tmpDisciplines;
                _tmpDisciplines = _cursor.getString(_cursorIndexOfDisciplines);
                _item = new LibraryCard(_tmpName,_tmpType,"",_tmpDisciplines,"",_tmpPoolCost,_tmpBloodCost,_tmpConvictionCost,"","");
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
