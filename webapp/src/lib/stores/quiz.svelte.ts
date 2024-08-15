import { api } from "$lib/api/api"

type QuestionID = number
type InputID = number
type Types = "TEXT" | "RADIO" | "CHECKBOX"

type Answer = {
	totalPoints: number
	points: number
	answers: string[]
}

type Question = {
	id: QuestionID
	title: string
	desc: string
	inputs: Input[]
}

type Input = {
	id: InputID
	type: Types
	label: string
	name: string
}

function createQuizStore() {
	let questions: Question[] = $state([])
	let answer: Answer | null = $state(null)

	return {
		get questions(): Readonly<Question[]> {
			return questions
		},
		get answer(): Readonly<Answer | null> {
			return answer
		},
		async loadQuestions() {
			try {
				const res = await api.get<Question[]>("/questions")
				questions = res.data
				return true
			} catch (error) {
				console.error("quizStore.getQuestions(), failed:", error)
				return false
			}
		},
		async checkAnswers(evt: Event) {
			try {
				const form = new FormData(evt.target as HTMLFormElement)
				const res = await api.post<Answer>("/answers", form)
				answer = res.data
				return true
			} catch (error) {
				console.error("quizStore.checkAnswers() failed:", error)
				return false
			}
		},
		reset() {
			questions = []
			answer = null
		},
	}
}

export const quizStore = createQuizStore()
