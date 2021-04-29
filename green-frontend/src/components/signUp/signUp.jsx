import {useCallback, useRef, useState} from 'react';
import styles from './signUp.module.css';
import Button from "../button/button";
import axios from "axios";
import {API_BASE_URL} from "../../util/constants";
import Header from "../header/header";
import Footer from "../footer/footer";
import styled from "@emotion/styled";
import useInput from "../../util/useInput.js";
import {useHistory} from "react-router";

const Label = styled.label`
  margin-bottom: 16px;
  & > span {
    display: block;
    text-align: left;
    padding-bottom: 8px;
    font-size: 15px;
    cursor: pointer;
    line-height: 1.4;
    font-weight: 700;
  }
`;

const Error = styled.div`
  color: #e01e5a;
  margin: 8px 0 16px;
  font-weight: bold;
`;

export const Success = styled.div`
  color: #2eb67d;
  font-weight: bold;
`;

const SignUp = () => {
    const [name, onChangeName, setName] = useInput('');
    const [password, setPassword] = useState('');
    const [email, onChangeEmail, setEmail] = useInput('');
    const [passwordCheck, setPasswordCheck] = useState('');
    const [mismatchError, setMismatchError] = useState(false);
    const [signUpError, setSignUpError] = useState('');
    const [signUpSuccess, setSignUpSuccess] = useState(false);
    const history = useHistory();

    const onChangePassword = useCallback((e) => {
        setPassword(e.target.value);
        setMismatchError(e.target.value !== passwordCheck);
    }, [passwordCheck]);

    const onChangePasswordCheck = useCallback((e) => {
        setPasswordCheck(e.target.value);
        setMismatchError(e.target.value !== password);
    }, [password]);

    const goToHome = (userData) => {
        history.push({
            pathname: '/',
            state: {data: userData},
        });
    };

    const onSubmit = event => {
        event.preventDefault();
        setSignUpSuccess(false);

        const signUpForm = {
            name: name || '',
            email: email || '',
            password: password || '',
        };

        const headers = new Headers({
            'Content-Type': 'application/json',
        });

        axios.request({
            method: 'POST',
            url: API_BASE_URL + 'join',
            headers: headers,
            data: {
                "name": signUpForm.name,
                "principal": signUpForm.email,
                "credentials": signUpForm.password
            },
        }).then( response => {
                console.log(response.data);
                setSignUpSuccess(true);
                goToHome(response.data);
        }).catch((error) => {
                console.log(error.response);
                setSignUpError(error.response.data);
        })
    };

    return (
        <div className={styles.container}>
            <Header />
            <form className={styles.form}>
                <h4 className="login-title">
                    💖 그린이에 오신 것을 환영합니다!
                </h4>
                <div className={styles.list}>
                    <input className={styles.input} type="text" name="name" value={name} onChange={onChangeName} placeholder="Name"/>
                    <input className={styles.input} type="text" name="email" value={email} onChange={onChangeEmail} placeholder="Email"/>
                    <input className={styles.input} type="password" name="password" value={password} onChange={onChangePassword} placeholder="password"/>
                    <input className={styles.input} type="password" name="password" value={passwordCheck} onChange={onChangePasswordCheck} placeholder="passwordCheck"/>
                    <Button className={styles.button} name='회원가입' onClick={onSubmit} />
                </div>
                <Label>
                    {mismatchError && <Error>비밀번호가 일치하지 않습니다.</Error>}
                    {signUpError && <Error>{signUpError}</Error>}
                    {signUpSuccess && <Success>회원가입 되었습니다. 로그인해주세요.</Success>}
                </Label>
            </form>
            <Footer />
        </div>

    )
};

export default SignUp;