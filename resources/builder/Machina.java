package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Machina extends _Default{
	public Machina() {
		super("Machina", "kunagiri");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5523};
				break;
			case HP:
				ids = new int[]{5521, 5522};
				break;
			case S1:
				ids = new int[]{5516};
				break;
			case S2:
				ids = new int[]{5527};
				break;
			case EX:
				ids = new int[]{5260};
				break;
			case AA:
				ids = new int[]{5531};
				break;
			case LD:
				ids = new int[]{10990};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}