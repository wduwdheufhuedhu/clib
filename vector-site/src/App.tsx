import './App.css'
import { CareersBand } from './components/CareersBand'
import { FeaturedWork } from './components/FeaturedWork'
import { Footer } from './components/Footer'
import { Header } from './components/Header'
import { Hero } from './components/Hero'
import { Insights } from './components/Insights'
import { Services } from './components/Services'

export default function App() {
  return (
    <div className="page">
      <Header />
      <main>
        <Hero />
        <FeaturedWork />
        <Services />
        <Insights />
        <CareersBand />
      </main>
      <Footer />
    </div>
  )
}
