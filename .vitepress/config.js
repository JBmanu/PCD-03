import {defineConfig} from 'vitepress'
import {withMermaid} from "vitepress-plugin-mermaid";

let reportSimulationCar = '/part1-simulation-car/doc'
let reportSudokuMOM = '/part2A-sudoku-mom/doc'
let reportSudokuJavaRMI = '/part2B-sudoku-rmi/doc'
let reportGuessNumber = '/part3-guess-number/doc'

// https://vitepress.dev/reference/site-config


export default withMermaid(
    defineConfig({
        base: '/PCD-03/',
        title: "PCD-03",
        description: "Programmazione Concorrente e Distribuita",
        themeConfig: {
            // https://vitepress.dev/reference/default-theme-config
            nav: [
                {text: 'Home', link: '/'},
            ],

            sidebar: [
                {
                    text: 'Report',
                    items: [
                        {text: 'Contesto', link: `${reportSimulationCar}/report`},
                        {text: 'Scoping', link: `${reportSudokuMOM}/report`},
                        {text: 'Planning', link: `${reportSudokuJavaRMI}/report`},
                        {text: 'Launching/Execution', link: `${reportGuessNumber}/report`},
                    ]
                }
            ],

            socialLinks: [
                {icon: 'github', link: 'https://github.com/JBmanu/PCD-03'}
            ]
        }
    })
)
