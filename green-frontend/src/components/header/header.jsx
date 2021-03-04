import { memo } from 'react';
import styles from './header.module.css';

const Header = memo(() => (
    <header className={styles.header}>
        <button className={styles.signUp}>회원가입</button>
        <button className={styles.logout}>로그인</button>
        <img className={styles.logo} src="/images/logo.png" alt="logo"/>
        <h1 className={styles.title}>Green 그린이</h1>
    </header>
));

export default Header;