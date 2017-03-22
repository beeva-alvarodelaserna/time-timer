import React, {Component} from 'react'
import {AppRegistry, StyleSheet, ScrollView, StatusBar, Text, View, Alert} from 'react-native';
import PieChart from 'react-native-pie-chart';
import Button from './Button'

const onStartTimer = () => {
    this.setInterval(function () {
        if (series[0] > 0) {
            series[0]--;
            series[1]++;
            this._timer.setNativeProps(series);
        }
    }, 1000);

};

let SimpleSession = ({_goBack}) => (
    <View style={styles.container}>
        <Button onPress={_goBack} label='Ir patrás' any="" title=""/>

        <View style={styles.container}>
            <StatusBar
                hidden={true}
            />
            <Text style={styles.title}>Basic</Text>
            <MyPie ref={component => this._timer = component}
                   series={series}/>

        </View>
        <Button onPress={onStartTimer} label='Empezar' any="" title=""/>
    </View>
);

class MyPie extends React.Component {

    constructor(props) {
        super(props);
        this.state        = { series: series } ;
        this.setNativeProps = this.setNativeProps.bind(this);
    }

    setNativeProps(nativeProps) {
        let message = JSON.stringify(nativeProps);
        //Alert.alert(message);
        // this._myPie.setNativeProps(nativeProps);
        this._myPie.setState({series: series});
        //this.refs.pie.setNativeProps(nativeProps);
    }

    render() {
        return (
            <View ref={component => this._myPie = component} {...this.props}>
                <PieChart
                    chart_wh={chart_wh}
                    series={this.state.series}
                    sliceColor={sliceColor}/>
                <Text>{JSON.stringify(this.state.series)}</Text>
            </View>
        )
    }
}

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
let series = [12, 32];
const sliceColor = ['#F44336', '#2196F3', '#FFEB3B', '#4CAF50', '#FF9800'];

export default SimpleSession