import React, { Component } from 'react'
import { AppRegistry, StyleSheet, ScrollView , StatusBar, Text, View } from 'react-native';
import PieChart from 'react-native-pie-chart';
import Button from './Button'

const About = ({_goBack}) => (
    <View style={styles.container}>
        <Button onPress={_goBack} label='Ir patrás' any="" title=""/>
        <ScrollView style={{flex: 1}}>
            <View style={styles.container}>
                <StatusBar
                    hidden={true}
                />
                <Text style={styles.title}>Basic</Text>
                <PieChart
                    chart_wh={chart_wh}
                    series={series}
                    sliceColor={sliceColor}
                />
                <Text style={styles.title}>Doughnut</Text>
                <PieChart
                    chart_wh={chart_wh}
                    series={series}
                    sliceColor={sliceColor}
                    doughnut={true}
                    coverRadius={0.45}
                    coverFill={'#FFF'}
                />
            </View>
        </ScrollView>
    </View>
);

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center'
    },
    title: {
        fontSize: 24,
        margin: 10
    }
});

const chart_wh = 250;
//const series = [12, 32, 12, 78, 53];
const series = [12, 32];
const sliceColor = ['#F44336','#2196F3','#FFEB3B', '#4CAF50', '#FF9800'];

export default About