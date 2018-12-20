import React, { Component } from 'react';
import Button from 'react-bootstrap/lib/Button';
import Container from 'react-bootstrap/lib/Container';
import Row from 'react-bootstrap/lib/Row';
import Col from 'react-bootstrap/lib/Col';
import Form from 'react-bootstrap/lib/Form';
import Card from 'react-bootstrap/lib/Card'

class Login extends Component {
    render() {
        return (
            <Container>
                <Row className="mt-5 pt-5"></Row>
                <Row className="mt-5 pt-5">
                    <Col md={{ span: 4, offset: 4 }} >
                        <Card>
                            <Card.Body>
                                <Form>
                                    <Form.Group controlId="formUsername">
                                        <Form.Control type="text" placeholder="Username" />
                                    </Form.Group>
                                    <Form.Group controlId="formPassword">
                                        <Form.Control type="password" placeholder="Password" />
                                    </Form.Group>
                                    <Form.Group controlId="formRememberMe">
                                        <Form.Check type="checkbox" label="Remember me" />
                                    </Form.Group>
                                    <Button variant="outline-primary" type="submit" block>
                                        Submit
                            </Button>
                                </Form>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default Login;
