import React, { Component } from 'react';
import Alert from 'react-bootstrap/lib/Alert';

class BSAlert extends Component {
  render() {

    const alerts = [
      'primary',
      'secondary',
      'success',
      'danger',
      'warning',
      'info',
      'light',
      'dark',
    ].map((variant, idx) => (
      <Alert key={idx} variant={variant}>
        This is a {variant} alert—check it out!
      </Alert>
    ));
      return alerts;
    // return (
    //   <Alert dismissible variant="danger">
    //     <Alert.Heading>Oh snap! You got an error!</Alert.Heading>
    //     <p>
    //       Change this and that and try again. Duis mollis, est non commodo luctus,
    //       nisi erat porttitor ligula, eget lacinia odio sem nec elit. Cras mattis
    //       consectetur purus sit amet fermentum.
    //     </p>
    //   </Alert>
    // );
  }
}

export default BSAlert;
