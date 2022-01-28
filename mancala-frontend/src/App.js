import './App.css';
import * as React from 'react';
import Grid from '@mui/material/Grid';
import { createTheme, ThemeProvider, styled } from '@mui/material/styles';
import { createGameService, gameActionService } from './Services/Api';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';



const Pit = styled(Button)(({ theme }) => ({
  ...theme.typography.body2,
  textAlign: 'center',
  color: theme.palette.text.secondary,
  width: '100%',
  height: '100px',
  lineHeight: '60px',
  borderWidth: '2px'
}));

const LargPit = styled(Button)(({ theme }) => ({
  ...theme.typography.body2,
  textAlign: 'center',
  color: theme.palette.text.secondary,
  height: '95%',
  lineHeight: '60px',
  width: '100%',
}));


const lightTheme = createTheme({
  palette: {
    mode: 'light', action: {
      disabledBackground: 'black',
      disabled: 'black'
    }
  }
});



function App() {
  const [pitValues, setPitvlaues] = React.useState({
    1: 0,
    2: 0,
    3: 0,
    4: 0,
    5: 0,
    6: 0,
    7: 0,
    8: 0,
    9: 0,
    10: 0,
    11: 0,
    12: 0,
    13: 0,
    14: 0
  })

  const [dialogOpen, setDialogOpen] = React.useState(false);
  const [dialogMessage, setDialogMessage] = React.useState('');
  const [isLoading, setIsloading] = React.useState(false);
  const [gameId, setGameId] = React.useState(0);
  const [player, setPlayer] = React.useState(1);
  const [winner, setWinner] = React.useState(null);


  const createNewGame = async () => {
    setIsloading(true);
    const response = await createGameService();
    if (response.status !== 201) {
      const message = response.data?.message;
      setDialogMessage(message);
      setDialogOpen(true);
      
    }
    setGameId(response.data.gameId);
    const stoneCount = response.data.stonesPerPit;
    setPitvlaues({
      
      1: stoneCount,
      2: stoneCount,
      3: stoneCount,
      4: stoneCount,
      5: stoneCount,
      6: stoneCount,
      7: 0,
      8: stoneCount,
      9: stoneCount,
      10: stoneCount,
      11: stoneCount,
      12: stoneCount,
      13: stoneCount,
      14: 0
    })
    setIsloading(false);
  };

  const playGame = async (pitId) => {
    setIsloading(true);
    const response =  await gameActionService(gameId, pitId);
    if (response.status !== 200) {
      const message = response.data?.message;
      setDialogMessage(message);
      setDialogOpen(true);
      setIsloading(false);
    }else{
    setPitvlaues(response.data.status);
    setPlayer(response.data.currentPlayer === 'MANCALA_PLAYER_ONE' ? 1 : 2);
    setIsloading(false);
    if(response.data.winner !== null){
      setWinner(response.data.winner);
    }
  }
  }

  const handleClose = () => {
    setDialogOpen(false);
  };


  React.useEffect(() => {
    createNewGame()
  }, []);
  if (isLoading) {
    return (
      <div className="App">
        <CircularProgress />

      </div>)
  } else {


    return (
      <div className="App">
        <h1>Welcome to Mancala</h1>
        <h3>Player 2</h3>
        <ThemeProvider theme={lightTheme}>
          <Grid container spacing={2}>

            <Grid item xs={2} >
              <LargPit disabled variant="outlined" elevation={5} >
                <h3>{pitValues[14]}</h3>
              </LargPit>
            </Grid>
            <Grid item xs={8} >
              <Grid container spacing={2}>

                {[ 13, 12, 11, 10, 9, 8, 1, 2, 3, 4, 5, 6,].map((ele) => (
                  <Grid key={ele} item xs={2} >
                    <Pit  disabled={(player === 1 && ele > 7) || (player === 2 && ele < 7)} variant="outlined" elevation={5} onClick={() => { playGame(ele) }}>
                      <h3>{pitValues[ele]}</h3>
                    </Pit>
                  </Grid>
                ))}

              </Grid>

            </Grid>
            <Grid item xs={2} >
              <LargPit disabled variant="outlined" elevation={5}>
                <h3>{pitValues[7]}</h3>
              </LargPit>
            </Grid>

          </Grid>

          <h3>Player 1</h3>

          <Dialog
            open={dialogOpen}

            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
          >
            <DialogTitle id="alert-dialog-title">
              {"Something went wrong Please readload the game to start again."}
            </DialogTitle>
            <DialogContent>
              <DialogContentText id="alert-dialog-description">
                {`Error: ${dialogMessage}`}
              </DialogContentText>
            </DialogContent>
            <DialogActions>

              <Button onClick={() => { window.location.reload(); }} autoFocus>
                Reload
              </Button>
              <Button onClick={handleClose} autoFocus>
                Close
              </Button>
            </DialogActions>
          </Dialog>

          <Dialog
            open={winner != null}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
          >
            <DialogTitle id="alert-dialog-title">
              Congragulations
            </DialogTitle>
            <DialogContent>
              <DialogContentText id="alert-dialog-description">
                {`THE WINNER IS : ${winner}`}
              </DialogContentText>
            </DialogContent>
            <DialogActions>

              <Button onClick={() => { window.location.reload(); }} autoFocus>
                Play Again
              </Button>
            </DialogActions>
          </Dialog>

        </ThemeProvider>
      </div>
    );
  }
}





export default App;
