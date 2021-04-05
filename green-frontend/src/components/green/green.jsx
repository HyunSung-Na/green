import styles from './green.module.css';
import Header from "../header/header";
import Footer from "../footer/footer";

const Green = () => {
    return (
        <section className={styles.green}>
            <Header />
            <div className={styles.container}>
                <h1>🚀 그린이에 오신 것을 환영합니다.</h1>
            </div>
            <Footer />
        </section>
    )
};

export default Green;