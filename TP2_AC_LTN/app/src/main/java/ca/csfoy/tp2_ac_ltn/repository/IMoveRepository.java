package ca.csfoy.tp2_ac_ltn.repository;

import ca.csfoy.tp2_ac_ltn.data.MoveData;
import ca.csfoy.tp2_ac_ltn.exceptions.DatabaseExceptions;
public interface IMoveRepository extends Repository<MoveData> {

    void delete(MoveData myObject) throws DatabaseExceptions;
    MoveData findRandomMove() throws DatabaseExceptions;



}
