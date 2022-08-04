import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { CustomerList } from './components/CustomerList';
import { Customer } from './components/Customer';

function App() {
  return (
    <Container>
      <Router>
        <Switch>
          <Route path='/customers/:customerId'>
            <Customer />
          </Route>
          <Route path='/customers'>
            <CustomerList />
          </Route>
          <Route path='/'>
            <Redirect to="/customers" />
          </Route>
        </Switch>
      </Router>
    </Container>
  );
}

export default App;
