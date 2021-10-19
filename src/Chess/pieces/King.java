package Chess.pieces;

import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.Color;
import boardgame.Board;
import boardgame.Position;

public class King extends ChessPiece {
    
    private ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    public boolean canMove(Position position){
        ChessPiece p = (ChessPiece)getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    public boolean testCastlingMove(Position position){
        ChessPiece rookCastling = (ChessPiece)getBoard().piece(position);

        return rookCastling instanceof Rook && rookCastling.getColor() == getColor() && rookCastling.getMoveCount() == 0;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean [][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0,0);

        //above
        p.setValues(position.getRow() - 1, position.getColumn());
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //below
        p.setValues(position.getRow() + 1, position.getColumn());
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //right
        p.setValues(position.getRow(), position.getColumn() + 1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //left
        p.setValues(position.getRow(), position.getColumn() - 1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //northwest
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //northeast
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //southwest
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //southeast
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        if(getBoard().positionExists(p) && canMove(p)){
            mat[p.getRow()][p.getColumn()] = true;
        }

        //#SPECIAL MOVE CASTLING
        //(WHITE PIECES POINT OF VIEW)
        if(getMoveCount() == 0 && !chessMatch.getCheck()){

            //#SPECIAL MOVE KINGSIDE ROOK
            Position kingRightSideRook = new Position(position.getRow(), position.getColumn() + 3);
            if(testCastlingMove(kingRightSideRook)){
                Position rightSideOfTheKing = new Position(position.getRow(), position.getColumn() + 1);
                Position leftSideOfTheRightRook = new Position(position.getRow(), position.getColumn() + 2);

                if( getBoard().piece(rightSideOfTheKing) == null && getBoard().piece(leftSideOfTheRightRook) == null){
                    mat[position.getRow()][position.getColumn() + 2] = true;
                    }
            }

            //#SPECIAL MOVE QUEENSIDE ROOK
            Position kingLeftSideRook = new Position(position.getRow(), position.getColumn() - 4);
            if(testCastlingMove(kingLeftSideRook)){
                Position leftSideOfTheKing = new Position(position.getRow(), position.getColumn() - 1);
                Position middleSpaceOnTheLeftKingSide = new Position(position.getRow(), position.getColumn() - 2);
                Position rightSideOfTheLeftRook = new Position(position.getRow(), position.getColumn() - 3);

                if( getBoard().piece(leftSideOfTheKing ) == null && getBoard().piece(middleSpaceOnTheLeftKingSide) == null && getBoard().piece(rightSideOfTheLeftRook) == null){
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }
            }

        }

        return mat;
    }

    @Override
    public String toString(){
        return "K";
    }
}
