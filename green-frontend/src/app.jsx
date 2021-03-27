import React from "react";
import styles from './app.module.css';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import Green from "./components/green/green";
import SignUp from "./components/signUp/signUp";

function App() {
    return (
        <div className={styles.app}>
            <BrowserRouter>
                <Switch>
                    <Route exact path="/">
                        <Green />
                    </Route>
                    <Route exact path="/join">
                        <SignUp />
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

export default App;
