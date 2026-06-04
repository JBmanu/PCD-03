import {defineConfig} from 'vitepress'
import {withMermaid} from "vitepress-plugin-mermaid";

let reportPath = '/report'
let scopingPath = reportPath + '/scoping'
let planningPath = reportPath + '/planning'
// https://vitepress.dev/reference/site-config


export default withMermaid(
    defineConfig({
        base: '/AthenaPlay-PM/',
        title: "AthenaPlay-PM",
        description: "Ulisse an Train Infrastructure Similator",
        themeConfig: {
            // https://vitepress.dev/reference/default-theme-config
            nav: [
                {text: 'Home', link: '/'},
            ],

            sidebar: [
                {
                    text: 'Report',
                    items: [
                        {text: 'Contesto', link: `${reportPath}/0-contesto`},
                        {text: 'Scoping', link: `${reportPath}/1-scoping`},
                        {text: 'Planning', link: `${reportPath}/2-planning`},
                        {text: 'Launching/Execution', link: `${reportPath}/3-launching-execution`},
                        {text: 'Monitoring E controlling', link: `${reportPath}/4-monitoring-E-controlling`},
                        {text: 'Closing', link: `${reportPath}/5-closing`},
                    ]
                },
                {
                    text: 'Allegati',
                    items: [
                        {
                            text: 'Scoping',
                            collapsed: true,
                            items: [
                                {text: '1-Meeting', link: `${scopingPath}/1-meeting`},
                                {text: '2-Meeting', link: `${scopingPath}/2-meeting`},
                                {text: '3-Meeting', link: `${scopingPath}/3-meeting`},
                                {text: 'Market Analysis', link: `${scopingPath}/Market-Analysis`},
                                {text: 'POS', link: `${scopingPath}/POS`},
                                {text: 'Risk Analysis', link: `${scopingPath}/Risk-Analysis`},
                                {text: 'Feasibility studies', link: `${scopingPath}/Feasibility-Studies`},
                                {text: 'RBS', link: `${scopingPath}/RBS`},
                                {text: 'SWOT Analysis', link: `${scopingPath}/SWOT-Analysis`},
                            ]
                        },
                        {
                            text: 'Planning',
                            collapsed: true,
                            items: [
                                {text: 'WBS', link: `${planningPath}/WBS`},
                                {text: 'PND', link: `${planningPath}/PND`},
                                {text: 'Gantt', link: `${planningPath}/Gantt`},
                                {text: 'RBS', link: `${planningPath}/RBS`},
                                {text: 'Risk Management', link: `${planningPath}/Risk-Management-Plan`},
                            ]
                        }
                    ]

                }
            ],

            socialLinks: [
                {icon: 'github', link: 'https://github.com/JBmanu/AthenaPlay-PM'}
            ]
        }
    })
)
