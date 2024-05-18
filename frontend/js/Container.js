import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import * as React from "react";
import {useEffect, useState} from "react";
import axios from "axios";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" href="https://mui.com/">
                This is not financial advice!
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

export default function Container() {

    const [tickers, setTickers] = useState(['AAPL', 'TSLA', 'AMZN']);
    const [report, setReport] = useState('');

    const reportBtn = document.querySelector('#report-button');

    function getAxiosConfiguration() {
        return {
            timeout: 15000,
            signal: AbortSignal.timeout(15000)
        };
    }

    useEffect(() => {
        console.log(report);
    }, [report])

    async function fetchStockData(e) {
        e.preventDefault();
        // do something with loading animation
        let tickersStr = tickers.join(', ');
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(
                    axios.get(`/warbucks/api/predict?tickers=${tickersStr}`, getAxiosConfiguration())
                        .then(response => {
                            setReport(response.data);
                            if (response.data !== null) {
                                return response;
                            }
                        })
                        .catch(error => {
                            console.log(error);
                        })
                );
            }, 1000);
        });

    }

    return (
        <section>
            <form id="ticker-input-form">
                <label htmlFor="ticker-input"> Add up to 3 stock tickers below to get a super accurate stock predictions report👇 </label>
                <div>
                    <input type="text" id="ticker-input" placeholder="AMZN"/>
                    <button>+</button>
                </div>
            </form>
            <p>Your tickers will appear here...</p>
            <button onClick={fetchStockData} id='report-button'>Generate Report</button>
            <p>This is not financial advice!</p>
            <div>
                <Copyright />
            </div>
        </section>
    );
}
