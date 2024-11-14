import React from 'react';
import { ResponsiveLine } from "@nivo/line";

const EloChart = ({ ratingHistory }) => {
    console.log("EloChart is rendered with data:", ratingHistory);

    if (!ratingHistory || ratingHistory.length === 0) {
        return <p style={{ margin: '20px', fontSize: '16px' }}>No rating history to display.</p>;
    }

    const chartData = [
        {
            id: 'Glicko Rating',
            data: ratingHistory.map(item => ({
                x: new Date(item.date),
                y: item.glickoRating
            }))
        }
    ];

    return (
        <div style={{ height: '600px', width: '600px' }}>
            <ResponsiveLine
                data={chartData}
                margin={{ top: 50, right: 110, bottom: 50, left: 60 }}
                xScale={{ type: 'time' }}
                yScale={{ type: 'linear', min: 'auto', max: 'auto', stacked: true, reverse: false }}
                axisBottom={{
                    tickSize: 5,
                    tickPadding: 5,
                    tickRotation: 0,
                    format: '%b %d, %Y',
                    legend: 'Date',
                    legendOffset: 36,
                    legendPosition: 'middle',
                }}
                axisLeft={{
                    tickSize: 5,
                    tickPadding: 5,
                    tickRotation: 0,
                    legend: 'Glicko Rating',
                    legendOffset: -40,
                    legendPosition: 'middle',
                }}
                colors={{ scheme: 'category10' }}
                pointSize={10}
                pointColor={{ theme: 'background' }}
                pointBorderWidth={1}
                pointBorderColor={{ from: 'serieColor' }}
                pointLabel="data.yFormatted"
                pointLabelYOffset={-12}
                enableTouchCrosshair={true}
                useMesh={true}
                theme={{
                    textColor: 'var(--primary)', // daisyUI primary text color
                    axis: {
                        ticks: {
                            text: {
                                fill: 'var(--neutral-content)', // daisyUI neutral text color
                            }
                        }
                    },
                    grid: {
                        line: {
                            stroke: 'var(--base-300)', // light grid lines
                            strokeWidth: 1
                        }
                    }
                }}
            />
        </div>
    );
}

export default EloChart;
