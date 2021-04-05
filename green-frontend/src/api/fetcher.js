import axios from 'axios';

const fetcher = ({ url, apiKey }) => {
    axios.get(url, {
        headers: {
            api_key: `Bearer ${apiKey}`
        },
        withCredentials: true,
    },)
    .then((response) => response.data)
    .catch((e) => console.log(e))};

export default fetcher;