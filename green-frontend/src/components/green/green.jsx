import React from 'react';
import styles from './green.module.css';
import Header from "../header/header";
import Footer from "../footer/footer";

const Green = () => {
    return (
        <section className={styles.green}>
            <Header />
            <div className={styles.container}>

            </div>
            <Footer />
        </section>
    )
};

export default Green;