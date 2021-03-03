import React from "react";
import styles from './app.module.css';
import {BrowserRouter, Switch, Route} from "react-router-dom";
import Green from "./components/green/green";

function App() {
    return (
        <div className={styles.app}>
            <BrowserRouter>
                <Switch>
                    <Route exact path="/">
                        <Green />
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

export default App;
