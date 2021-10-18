package Chess;

import Chess.chess.pieces.King;
import Chess.chess.pieces.Rook;
import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean isCheckMate;
    
    private List<Piece> piecesOnTheBoard= new ArrayList<>();
    private List<Piece> capturedPieces= new ArrayList<>();

    public ChessMatch(){
        board = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn(){
        return turn;
    }

    public Color getCurrentPlayer(){
        return currentPlayer;
    }

    public boolean getCheck(){
        return check;
    }

    public boolean getisCheckMate(){
        return isCheckMate;
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for(int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }

        return mat;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private ChessPiece kingColor(Color color){
        List<Piece> listOfKings = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : listOfKings){
            if(p instanceof King ) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + "king on the board");
    }

    private Color oponnentColor(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void nexTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public  boolean[][] possibleMove(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece perfomanceChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source,target);

        if(testCheckState(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check =(testCheckState(oponnentColor(currentPlayer))) ? true : false;

        if(testIsCheckMate(oponnentColor(currentPlayer))){
            isCheckMate = true;
        } else {
            nexTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();

        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if(capturedPiece != null){
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        return capturedPiece;

    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if(capturedPiece != null){
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private void validateSourcePosition(Position source){
        if(!board.therIsAPiece(source)){
            throw new ChessException("There is no piece on source position");
        }

        if(currentPlayer != ((ChessPiece)board.piece(source)).getColor()){
            throw new ChessException("The chosen piece is not yours");
        }

        if(!board.piece(source).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("The piece chosen can't move to target position");
        }
    }

    private boolean testCheckState(Color color){
        Position kingPosition = kingColor(color).getChessPosition().toPosition();

        List<Piece> oponnentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == oponnentColor(color)).collect(Collectors.toList());

        for(Piece p : oponnentPieces){
            boolean[][] mat = p.possibleMoves();

            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private boolean testIsCheckMate(Color color){
        if(!testCheckState(color)){
            return false;
        }

        List<Piece> alliedPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());

        for(Piece p : alliedPieces){
            boolean[][] mat = p.possibleMoves();

            for(int i = 0; i < board.getRows(); i++){
                for(int j = 0; j < board.getColumns(); j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);

                        Piece capturedPiece = makeMove(source, target);

                        boolean testCheck = testCheckState(color);
                        undoMove(source, target, capturedPiece);

                        if(!testCheck){
                            return false;
                        }

                    }
                }
            }
        }

        return true;
    }

    private void initialSetup(){
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }
}
