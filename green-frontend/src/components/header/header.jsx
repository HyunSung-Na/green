import { memo } from 'react';
import styles from './header.module.css';
import {useHistory} from "react-router";

const Header = memo(() => {
    const history = useHistory();

    const onClickSignUp = () => {
        history.push('/join');
    };

    const onClickLogin = () => {
        history.push('/login');
    }

    return (
    <header className={styles.header}>
        <button className={styles.signUp} onClick={onClickSignUp}>회원가입</button>
        <button className={styles.logout} onClick={onClickLogin}>로그인</button>
        <img className={styles.logo} src="/images/logo.png" alt="logo"/>
        <h1 className={styles.title}>Green 그린이</h1>
    </header>
    )
});

export default Header;