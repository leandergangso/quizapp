<script lang="ts">
	import { quizStore } from "$lib/stores/quiz.svelte"
	import { onMount } from "svelte"

	onMount(() => {
		quizStore.reset()
		quizStore.loadQuestions()
	})
</script>

<main>
	<h1>Welcome to our Quiz</h1>
	<p>
		Answer each question as they are presented. You will get the answers at the end of the quiz. You can at any moment
		go back and change your answers. Good luck, may you get full marks!
	</p>
	<a
		href="/quiz"
		class="bg-blue-500 text-white">I'm ready!</a
	>
</main>
