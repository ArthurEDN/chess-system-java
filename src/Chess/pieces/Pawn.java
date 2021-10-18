package Chess.pieces;

import Chess.ChessPiece;
import Chess.Color;
import boardgame.Board;
import boardgame.Position;

public class Pawn extends ChessPiece {

    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean [][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0,0);

        if(getColor() == Color.WHITE){

            // 1 above step
            p.setValues(position.getRow() - 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().therIsAPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }

            //2 above steps
            p.setValues(position.getRow() - 2, position.getColumn());
            Position auxPiece2 = new Position(position.getRow() - 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().therIsAPiece(p) && getBoard().positionExists(auxPiece2) && !getBoard().therIsAPiece(auxPiece2) && getMoveCount() == 0){
                mat[p.getRow()][p.getColumn()] = true;
            }

            //northwest
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }

            //northeast
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
        } else {

            // 1 below step
            p.setValues(position.getRow() + 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().therIsAPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }

            //2 below steps
            p.setValues(position.getRow() + 2, position.getColumn());
            Position auxPiece2 = new Position(position.getRow() + 1, position.getColumn());
            if(getBoard().positionExists(p) && !getBoard().therIsAPiece(p) && getBoard().positionExists(auxPiece2) && !getBoard().therIsAPiece(auxPiece2) && getMoveCount() == 0){
                mat[p.getRow()][p.getColumn()] = true;
            }

            //southwest
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }

            //southeast
            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if(getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }

        }

        return mat;
    }

    @Override
    public String toString(){
        return "P";
    }
}
