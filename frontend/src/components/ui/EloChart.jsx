import React from 'react';
import { ResponsiveLine } from "@nivo/line";

const EloChart = ({ ratingHistory }) => {
    console.log("EloChart is rendered with data:", ratingHistory);

    if (!ratingHistory || ratingHistory.length === 0) {
        return <p style={{ margin: '20px', fontSize: '16px' }}>No rating history to display.</p>;
    }

    // Transform ratingHistory data into a format that Nivo expects
    const chartData = [
        {
            id: 'Glicko Rating',
            data: ratingHistory.map(item => ({
                x: new Date(item.date),  // Convert date string to Date object for x-axis
                y: item.glickoRating
            }))
        }
    ];

    return (
        <div style={{ height: '600px', width: '1000px' }}>
            <ResponsiveLine
                data={chartData}
                margin={{ top: 50, right: 110, bottom: 50, left: 60 }}
                xScale={{ type: 'time' }}
                yScale={{
                    type: 'linear',
                    min: 'auto',
                    max: 'auto',
                    stacked: true,
                    reverse: false
                }}
                axisTop={null}
                axisRight={null}
                axisBottom={{
                    tickSize: 5,
                    tickPadding: 5,
                    tickRotation: 0,
                    format: '%b %d, %Y',  // Format the date on the x-axis
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
                legends={[
                    {
                        anchor: 'bottom-right',
                        direction: 'column',
                        justify: false,
                        translateX: 100,
                        translateY: 0,
                        itemsSpacing: 0,
                        itemDirection: 'left-to-right',
                        itemWidth: 80,
                        itemHeight: 20,
                        itemOpacity: 0.75,
                        symbolSize: 12,
                        symbolShape: 'circle',
                        symbolBorderColor: 'rgba(0, 0, 0, .5)',
                        effects: [
                            {
                                on: 'hover',
                                style: {
                                    itemBackground: 'rgba(0, 0, 0, .03)',
                                    itemOpacity: 1
                                }
                            }
                        ]
                    }
                ]}
            />
        </div>
    );
}

export default EloChart;
