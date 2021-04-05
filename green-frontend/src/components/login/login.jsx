import useInput from '../../util/useInput.js';
import styles from './login.module.css';
import axios from 'axios';
import React, {useCallback, useRef, useState} from 'react';
import styled from "@emotion/styled";
import Header from "../header/header";
import Button from "../button/button";
import Footer from "../footer/footer";
import {API_BASE_URL} from "../../util/constants";
import useSWR from "swr";
import Fetcher from "../../api/fetcher";
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

export const LinkContainer = styled.p`
  font-size: 13px;
  color: #616061;
  margin: 0 auto 8px;
  width: 400px;
  max-width: 400px`;

const Error = styled.div`
  color: #e01e5a;
  margin: 8px 0 16px;
  font-weight: bold;
`;

export const Success = styled.div`
  color: #2eb67d;
  font-weight: bold;
`;

const LogIn = () => {
    const authHeader = 'api_key';
    const [logInError, setLogInError] = useState(false);
    const [principal, onChangePrincipal] = useInput('');
    const [credentials, onChangeCredentials] = useInput('');
    const [apiKey, setApiKey] = useState('');
    const {data, revalidate} = useSWR('http://localhost:8080/api/user', apiKey, Fetcher);
    const formRef = useRef();
    const history = useHistory();

    const goToHome = (userData) => {
        history.push({
            pathname: '/',
            state: {data: userData},
        });
    };

    const onSubmit = useCallback(
        (e) => {
            e.preventDefault();
            setLogInError(false);

            axios.post(
                    API_BASE_URL + 'auth',
                    { principal, credentials },
                    {
                        withCredentials: true,
                    },
                )
                .then((e) => {
                    setApiKey(e.data.response.jwtToken);
                    revalidate();
                    localStorage.setItem(authHeader, apiKey);
                    goToHome(data);
                })
                .catch((e) => {
                    setLogInError(e.response?.data?.statusCode === 401);
                });
        },
        [principal, credentials],
    );


    console.log(data);

    return (
        <div className={styles.container}>
            <Header />
            <form ref={formRef} className={styles.form}>
                <div className={styles.list}>
                    <input className={styles.input} type="text" name="email" value={principal} onChange={onChangePrincipal} placeholder="Email"/>
                    <input className={styles.input} type="password" name="password" value={credentials} onChange={onChangeCredentials} placeholder="password"/>
                    <Button className={styles.button} name='로그인' onClick={onSubmit} />
                </div>
                <Label>
                    {logInError && <Error>{logInError}</Error>}
                </Label>
            </form>
            <LinkContainer>
                아직 회원이 아니신가요?&nbsp;
                <a href="/join">회원가입 하러가기</a>
            </LinkContainer>
            <Footer />
        </div>
    );
};

export default LogIn;
